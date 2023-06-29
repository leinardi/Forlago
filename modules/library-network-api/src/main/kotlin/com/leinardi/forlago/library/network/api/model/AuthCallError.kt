package com.leinardi.forlago.library.network.api.model

sealed class AuthCallError {
    data class ApiError(val errorMessages: List<String>?) : AuthCallError()
    data class NetworkError(val errorMessage: String?) : AuthCallError()
    data class UnrecoverableError(val errorMessage: String? = null) : AuthCallError()
    object ReAuthenticationRequired : AuthCallError()
}
