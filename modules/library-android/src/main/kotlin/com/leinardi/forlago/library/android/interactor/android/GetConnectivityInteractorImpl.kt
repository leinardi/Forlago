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

package com.leinardi.forlago.library.android.interactor.android

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.leinardi.forlago.library.android.api.interactor.android.GetConnectivityInteractor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GetConnectivityInteractorImpl @Inject constructor(
    private val application: Application,
) : GetConnectivityInteractor {
    private val connectivityManager by lazy { application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    override operator fun invoke() = getState()

    private fun getState(): GetConnectivityInteractor.State {
        val networkCapabilities: NetworkCapabilities? = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return if (networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {
            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> GetConnectivityInteractor.State.Online(
                    GetConnectivityInteractor.Type.WIFI,
                )
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> GetConnectivityInteractor.State.Online(
                    GetConnectivityInteractor.Type.CELLULAR,
                )
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> GetConnectivityInteractor.State.Online(
                    GetConnectivityInteractor.Type.ETHERNET,
                )
                else -> GetConnectivityInteractor.State.Online(GetConnectivityInteractor.Type.UNKNOWN)
            }
        } else {
            GetConnectivityInteractor.State.Offline
        }
    }
}
