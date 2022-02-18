/*
 * Copyright 2022 Roberto Leinardi.
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

package com.leinardi.forlago.core.network.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.api.NormalizedCacheFactory
import com.apollographql.apollo3.cache.normalized.logCacheMisses
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.network.okHttpClient
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.leinardi.forlago.core.android.BuildConfig
import com.leinardi.forlago.core.android.interactor.android.GetConnectivityInteractor
import com.leinardi.forlago.core.preferences.interactor.GetEnvironmentInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val NETWORK_TIMEOUT_IN_SECONDS = 15L
    private const val MEMORY_CACHE_SIZE_IN_BYTES = 10 * 1024 * 1024
    private val CACHE_EXPIRES_IN_MILLIS = TimeUnit.HOURS.toMillis(1)

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
    fun provideJson() = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Provides
    @Singleton
    @IntoSet
    fun provideKotlinxSerializationConverterFactory(json: Json): Converter.Factory = json.asConverterFactory("application/json".toMediaType())

    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptorSet: Set<@JvmSuppressWildcards Interceptor>,
    ): OkHttpClient = OkHttpClient.Builder().apply {
        interceptorSet.forEach { addInterceptor(it) }
        retryOnConnectionFailure(false)
        callTimeout(NETWORK_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
    }.build()

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        converterFactorySet: Set<@JvmSuppressWildcards Converter.Factory>,
    ): Retrofit.Builder = Retrofit.Builder().apply {
        converterFactorySet.forEach { addConverterFactory(it) }
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
    fun provideApolloBuilder(
        httpClient: OkHttpClient,
        environment: GetEnvironmentInteractor.Environment,
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
}
