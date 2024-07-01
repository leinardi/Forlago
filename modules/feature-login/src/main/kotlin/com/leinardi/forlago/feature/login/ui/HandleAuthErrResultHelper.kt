package com.leinardi.forlago.feature.login.ui

import com.leinardi.forlago.library.network.api.model.AuthErrResult
import javax.inject.Inject

class HandleAuthErrResultHelper @Inject constructor() {
    suspend operator fun invoke(error: AuthErrResult, showError: suspend (error: String) -> Unit) {
        when (error) {
            is AuthErrResult.BadAuthentication -> showError("BadAuthentication")
            is AuthErrResult.NetworkError -> showError("NetworkError")
            is AuthErrResult.UnexpectedError -> showError("UnexpectedError")
        }
    }
}
