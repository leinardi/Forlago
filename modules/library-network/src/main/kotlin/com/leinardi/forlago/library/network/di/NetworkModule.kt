/*
 * Copyright 2024 Roberto Leinardi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.leinardi.forlago.library.network.di

import androidx.annotation.VisibleForTesting
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.api.NormalizedCacheFactory
import com.apollographql.apollo3.cache.normalized.logCacheMisses
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.network.okHttpClient
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.leinardi.forlago.library.android.api.coroutine.CoroutineDispatchers
import com.leinardi.forlago.library.android.api.interactor.android.GetConnectivityInteractor
import com.leinardi.forlago.library.network.BuildConfig
import com.leinardi.forlago.library.network.api.interactor.ClearApolloCacheInteractor
import com.leinardi.forlago.library.network.api.interactor.ReadCertificatePinningEnabledInteractor
import com.leinardi.forlago.library.network.api.interactor.ReadEnvironmentInteractor
import com.leinardi.forlago.library.network.api.interactor.StoreCertificatePinningEnabledInteractor
import com.leinardi.forlago.library.network.api.interactor.StoreEnvironmentInteractor
import com.leinardi.forlago.library.network.interactor.ClearApolloCacheInteractorImpl
import com.leinardi.forlago.library.network.interactor.ReadCertificatePinningEnabledInteractorImpl
import com.leinardi.forlago.library.network.interactor.ReadEnvironmentInteractorImpl
import com.leinardi.forlago.library.network.interactor.StoreCertificatePinningEnabledInteractorImpl
import com.leinardi.forlago.library.network.interactor.StoreEnvironmentInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [NetworkModule.BindModule::class])
@InstallIn(SingletonComponent::class)
open class NetworkModule {
    @Provides
    @Singleton
    @IntoSet
    fun provideHttpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor { Timber.v(it) }.also {
        it.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    @Singleton
    @IntoSet
    fun provideConnectivityInterceptor(
        getConnectivityInteractor: GetConnectivityInteractor,
    ): Interceptor = Interceptor { chain ->
        if (getConnectivityInteractor() is GetConnectivityInteractor.State.Offline) {
            Timber.d("Device offline")
            throw IOException("Device offline")
        } else {
            chain.proceed(chain.request())
        }
    }

    @Provides
    @Singleton
    fun provideAddStageCertificatePinning(
        environment: ReadEnvironmentInteractor.Environment,
        readCertificatePinningEnabledInteractor: ReadCertificatePinningEnabledInteractor,
    ): CertificatePinner = CertificatePinner.Builder().apply {
        environment.certificatePinningConfigs.forEach { certificate ->
            if (certificate.isExpiring()) {
                val message = "Certificates for domain '${certificate.domain}' have less than 1 month until expiration date!"
                if (BuildConfig.DEBUG) {
                    throw CertificateExpiringException(message)
                } else {
                    Timber.e(message)
                }
            }
            runBlocking {
                if (readCertificatePinningEnabledInteractor()) {
                    Timber.d("Certificate Pinning Enabled: $certificate")
                    certificate.hashes.forEach { add(certificate.domain, it.hash) }
                } else {
                    Timber.d("Certificate Pinning Disabled")
                }
            }
        }
    }.build()

    @Provides
    @Singleton
    fun provideJson() = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Provides
    @Singleton
    @IntoSet
    fun provideScalarsConverterFactory(): Converter.Factory = ScalarsConverterFactory.create()

    @Provides
    @Singleton
    @IntoSet
    fun provideKotlinxSerializationConverterFactory(json: Json): Converter.Factory = json.asConverterFactory("application/json".toMediaType())

    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptorSet: Set<@JvmSuppressWildcards Interceptor>,
        certificatePinner: CertificatePinner,
    ): OkHttpClient = OkHttpClient.Builder().apply {
        interceptorSet.forEach { addInterceptor(it) }
        certificatePinner(certificatePinner)
        retryOnConnectionFailure(false)
        callTimeout(NETWORK_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
    }.build()

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        converterFactorySet: Set<@JvmSuppressWildcards Converter.Factory>,
    ): Retrofit.Builder = Retrofit.Builder().apply {
        converterFactorySet
            .sortedBy { if (it is ScalarsConverterFactory) 0 else 1 }  // ScalarsConverterFactory must be the first
            .forEach { addConverterFactory(it) }
    }

    @Provides
    @Singleton
    @IntoSet
    fun provideApolloMemoryCache(): NormalizedCacheFactory = MemoryCacheFactory(
        maxSizeBytes = MEMORY_CACHE_SIZE_IN_BYTES,
        expireAfterMillis = CACHE_EXPIRES_IN_MILLIS,
    )

    @Provides
    @Singleton
    fun provideApolloClient(
        httpClient: OkHttpClient,
        environment: ReadEnvironmentInteractor.Environment,
        cacheFactorySet: Set<@JvmSuppressWildcards NormalizedCacheFactory>,
    ): ApolloClient {
        var chainedCacheFactory = cacheFactorySet.firstOrNull()
        if (cacheFactorySet.size > 1) {
            cacheFactorySet.drop(1).forEach { cacheFactory ->
                chainedCacheFactory = chainedCacheFactory?.chain(cacheFactory)
            }
        }
        return ApolloClient.Builder()
            .serverUrl(environment.graphQlUrl)
            .okHttpClient(httpClient)
            .logCacheMisses(log = { Timber.d("Apollo cache miss: $it") })
            .apply { chainedCacheFactory?.let { normalizedCache(it) } }
            .build()
    }

    @Provides
    @Singleton
    fun provideReadEnvironmentInteractorEnvironment(
        coroutineDispatchers: CoroutineDispatchers,
        readEnvironmentInteractor: ReadEnvironmentInteractor,
    ): ReadEnvironmentInteractor.Environment = readEnvironment(coroutineDispatchers, readEnvironmentInteractor)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    protected open fun readEnvironment(
        coroutineDispatchers: CoroutineDispatchers,
        readEnvironmentInteractor: ReadEnvironmentInteractor,
    ): ReadEnvironmentInteractor.Environment = runBlocking(coroutineDispatchers.io) { readEnvironmentInteractor() }

    @Module
    @InstallIn(SingletonComponent::class)
    internal interface BindModule {
        @Binds
        fun bindClearApolloCacheInteractor(bind: ClearApolloCacheInteractorImpl): ClearApolloCacheInteractor

        @Binds
        fun bindReadCertificatePinningEnabledInteractor(bind: ReadCertificatePinningEnabledInteractorImpl): ReadCertificatePinningEnabledInteractor

        @Binds
        fun bindReadEnvironmentInteractor(bind: ReadEnvironmentInteractorImpl): ReadEnvironmentInteractor

        @Binds
        fun bindStoreCertificatePinningEnabledInteractor(bind: StoreCertificatePinningEnabledInteractorImpl): StoreCertificatePinningEnabledInteractor

        @Binds
        fun bindStoreEnvironmentInteractor(bind: StoreEnvironmentInteractorImpl): StoreEnvironmentInteractor
    }

    companion object {
        private const val MEMORY_CACHE_SIZE_IN_BYTES = 10 * 1024 * 1024
        private const val NETWORK_TIMEOUT_IN_SECONDS = 30L
        private val CACHE_EXPIRES_IN_MILLIS = TimeUnit.HOURS.toMillis(1)
    }
}

private class CertificateExpiringException(msg: String) : Exception(msg)
