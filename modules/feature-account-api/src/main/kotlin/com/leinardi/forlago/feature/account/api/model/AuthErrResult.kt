package com.leinardi.forlago.feature.account.api.model

sealed class AuthErrResult {
    data class BadAuthentication(val code: Int) : AuthErrResult()
    data class NetworkError(val throwable: Throwable) : AuthErrResult()
    data class UnexpectedError(val throwable: Throwable, val code: Int? = null) : AuthErrResult()
}
