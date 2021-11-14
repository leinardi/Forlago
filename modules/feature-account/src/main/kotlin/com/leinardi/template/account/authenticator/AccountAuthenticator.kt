/*
 * Copyright 2021 Roberto Leinardi.
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

package com.leinardi.template.account.authenticator

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.accounts.NetworkErrorException
import android.app.Application
import android.os.Bundle
import androidx.core.os.bundleOf
import com.leinardi.template.account.BuildConfig
import com.leinardi.template.account.feature.AccountFeature
import com.leinardi.template.account.interactor.GetNewAccessTokenInteractor
import com.leinardi.template.android.ext.toLongDateTimeString
import com.leinardi.template.encryption.interactor.DecryptDeterministicallyInteractor
import com.leinardi.template.encryption.interactor.DecryptInteractor
import com.leinardi.template.encryption.interactor.EncryptDeterministicallyInteractor
import com.leinardi.template.feature.FeatureManager
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * +--------+                                           +---------------+
 * |        |--(A)------- Authorization Grant --------->|               |
 * |        |                                           |               |
 * |        |<-(B)----------- Access Token -------------|               |
 * |        |               & Refresh Token             |               |
 * |        |                                           |               |
 * |        |                            +----------+   |               |
 * |        |--(C)---- Access Token ---->|          |   |               |
 * |        |                            |          |   |               |
 * |        |<-(D)- Protected Resource --| Resource |   | Authorization |
 * | Client |                            |  Server  |   |     Server    |
 * |        |--(E)---- Access Token ---->|          |   |               |
 * |        |                            |          |   |               |
 * |        |<-(F)- Invalid Token Error -|          |   |               |
 * |        |                            +----------+   |               |
 * |        |                                           |               |
 * |        |--(G)----------- Refresh Token ----------->|               |
 * |        |                                           |               |
 * |        |<-(H)----------- Access Token -------------|               |
 * +--------+           & Optional Refresh Token        +---------------+
 */

@Singleton
class AccountAuthenticator @Inject constructor(
    application: Application,
    private val accountManager: AccountManager,
    private val decryptInteractor: DecryptInteractor,
    private val decryptDeterministicallyInteractor: DecryptDeterministicallyInteractor,
    private val encryptDeterministicallyInteractor: EncryptDeterministicallyInteractor,
    private val featureManager: FeatureManager,
    private val getNewAccessTokenInteractor: GetNewAccessTokenInteractor,
) : AbstractAccountAuthenticator(application) {
    /**
     * Returns a Bundle that contains the Intent of the activity that can be used to edit the
     * properties. In order to indicate success the activity should call response.setResult()
     * with a non-null Bundle.
     *
     * @param response used to set the result for the request. If the Constants.INTENT_KEY
     *   is set in the bundle then this response field is to be used for sending future
     *   results if and when the Intent is started.
     * @param accountType the AccountType whose properties are to be edited.
     * @return a Bundle containing the result or the Intent to start to continue the request.
     *   If this is null then the request is considered to still be active and the result should
     *   sent later using response.
     */
    override fun editProperties(
        response: AccountAuthenticatorResponse,
        accountType: String
    ): Bundle? {
        throw UnsupportedOperationException()
    }

    /**
     * Adds an account of the specified accountType.
     *
     * @param response to send the result back to the AccountManager, will never be null
     * @param accountType the type of account to add, will never be null
     * @param authTokenType the type of auth token to retrieve after adding the account, may be null
     * @param requiredFeatures a String array of authenticator-specific features that the added
     * account must support, may be null
     * @param options a Bundle of authenticator-specific options. It always contains
     * {@link AccountManager#KEY_CALLER_PID} and {@link AccountManager#KEY_CALLER_UID}
     * fields which will let authenticator know the identity of the caller.
     * @return a Bundle result or null if the result is to be returned via the response. The result
     * will contain either:
     * <ul>
     * <li> {@link AccountManager#KEY_INTENT}, or
     * <li> {@link AccountManager#KEY_ACCOUNT_NAME} and {@link AccountManager#KEY_ACCOUNT_TYPE} of
     * the account that was added, or
     * <li> {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} to
     * indicate an error
     * </ul>
     * @throws NetworkErrorException if the authenticator could not honor the request due to a
     * network error
     */
    override fun addAccount(
        response: AccountAuthenticatorResponse,
        accountType: String,
        authTokenType: String?,
        requiredFeatures: Array<out String>?,
        options: Bundle
    ): Bundle {
        Timber.d("addAccount()")
        val accountsByType = accountManager.getAccountsByType(ACCOUNT_TYPE)
        return if (accountsByType.isNotEmpty()) {
            // If an account is already exist, we consider it an error.
            val error = "Unable to add a new account: an account already exists on the device"
            Timber.w(error)
            getErrorBundle(AccountManager.ERROR_CODE_CANCELED, error)
        } else {
            val intent = getAuthenticatorActivityIntent(response, accountType, options, true).apply {
                putExtras(
                    bundleOf(
                        AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE to response,
                        AccountManager.KEY_ACCOUNT_TYPE to accountType,
                        AccountManager.KEY_USERDATA to options,
                        KEY_IS_NEW_ACCOUNT to true
                    )
                )
            }
            bundleOf(AccountManager.KEY_INTENT to intent)
        }
    }

    private fun getErrorBundle(code: Int, message: String) = bundleOf(
        AccountManager.KEY_ERROR_CODE to code,
        AccountManager.KEY_ERROR_MESSAGE to message
    )

    /**
     * Confirms that the user knows the password for an account to make extra sure they are the
     * owner of the account.
     * The user-entered password can be supplied directly, otherwise the authenticator for this
     * account type prompts the user with the appropriate interface. This method is intended for
     * applications which want extra assurance; for example, the phone lock screen uses this to let
     * the user unlock the phone with an account password if they forget the lock pattern.
     *
     * If the user-entered password matches a saved password for this account, the request is
     * considered valid; otherwise the authenticator verifies the password (usually by contacting
     * the server).
     *
     * This method may be called from any thread, but the returned AccountManagerFuture must not be used on the main thread.
     *
     * @param response to send the result back to the AccountManager, will never be null
     * @param account the account whose credentials are to be checked, will never be null
     * @param options a Bundle of authenticator-specific options, may be null
     * @return a Bundle result or null if the result is to be returned via the response. The result
     * will contain either:
     * <ul>
     * <li> {@link AccountManager#KEY_INTENT}, or
     * <li> {@link AccountManager#KEY_BOOLEAN_RESULT}, true if the check succeeded, false otherwise
     * <li> {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} to
     * indicate an error
     * </ul>
     * @throws NetworkErrorException if the authenticator could not honor the request due to a
     * network error
     */
    override fun confirmCredentials(
        response: AccountAuthenticatorResponse,
        account: Account,
        options: Bundle?
    ): Bundle? {
        Timber.d("confirmCredentials()")
        return null
    }

    /**
     * Gets an authtoken for an account.
     *
     * If not {@code null}, the resultant {@link Bundle} will contain different sets of keys
     * depending on whether a token was successfully issued and, if not, whether one
     * could be issued via some {@link android.app.Activity}.
     * <p>
     * If a token cannot be provided without some additional activity, the Bundle should contain
     * {@link AccountManager#KEY_INTENT} with an associated {@link Intent}. On the other hand, if
     * there is no such activity, then a Bundle containing
     * {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} should be
     * returned.
     * <p>
     * If a token can be successfully issued, the implementation should return the
     * {@link AccountManager#KEY_ACCOUNT_NAME} and {@link AccountManager#KEY_ACCOUNT_TYPE} of the
     * account associated with the token as well as the {@link AccountManager#KEY_AUTHTOKEN}. In
     * addition {@link AbstractAccountAuthenticator} implementations that declare themselves
     * {@code android:customTokens=true} may also provide a non-negative {@link
     * #KEY_CUSTOM_TOKEN_EXPIRY} long value containing the expiration timestamp of the expiration
     * time (in millis since the unix epoch), tokens will be cached in memory based on
     * application's packageName/signature for however long that was specified.
     * <p>
     * Implementers should assume that tokens will be cached on the basis of account and
     * authTokenType. The system may ignore the contents of the supplied options Bundle when
     * determining to re-use a cached token. Furthermore, implementers should assume a supplied
     * expiration time will be treated as non-binding advice.
     * <p>
     * Finally, note that for {@code android:customTokens=false} authenticators, tokens are cached
     * indefinitely until some client calls {@link
     * AccountManager#invalidateAuthToken(String,String)}.
     *
     * @param response to send the result back to the AccountManager, will never be null
     * @param account the account whose credentials are to be retrieved, will never be null
     * @param authTokenType the type of auth token to retrieve, will never be null
     * @param options a Bundle of authenticator-specific options. It always contains
     * {@link AccountManager#KEY_CALLER_PID} and {@link AccountManager#KEY_CALLER_UID}
     * fields which will let authenticator know the identity of the caller.
     * @return a Bundle result or null if the result is to be returned via the response.
     * @throws NetworkErrorException if the authenticator could not honor the request due to a
     * network error
     */
    @Suppress("TooGenericExceptionCaught")
    override fun getAuthToken(
        response: AccountAuthenticatorResponse,
        account: Account?,
        authTokenType: String,
        options: Bundle
    ): Bundle {
        Timber.d("getAuthToken()")
        return when {
            account == null -> getAccountParameterMissingBundle()
            account !in accountManager.getAccountsByType(ACCOUNT_TYPE) -> getAccountNotFoundBundle()
            authTokenType != AUTHTOKEN_TYPE -> getWrongAuthTokenTypeBundle(authTokenType)
            else -> getAuthToken(account, authTokenType, response, options)
        }
    }

    private fun getAuthToken(
        account: Account,
        authTokenType: String,
        response: AccountAuthenticatorResponse,
        options: Bundle
    ): Bundle {
        var expiryInMillis = accountManager.getUserData(account, KEY_CUSTOM_TOKEN_EXPIRY)?.toLongOrNull() ?: 0
        var accessToken = accountManager.peekAuthToken(account, authTokenType)?.let { runBlocking { decryptDeterministicallyInteractor(it) } }
        val isTokenExpired = System.currentTimeMillis() - expiryInMillis > 0

        var errorBundle: Bundle? = null
        if (accessToken.isNullOrEmpty() || isTokenExpired) {
            Timber.d("Access token missing or expired")
            accessToken = null
            accountManager.invalidateAuthToken(account.type, authTokenType)
            val refreshToken: String? = accountManager.getPassword(account)?.let { runBlocking { decryptInteractor(it) } }
            if (!refreshToken.isNullOrEmpty()) {
                Timber.d("Refreshing the access token...")
                when (val result = runBlocking { getNewAccessTokenInteractor(refreshToken) }) {
                    is GetNewAccessTokenInteractor.Result.Success -> {
                        Timber.d("Access token successfully refreshed")
                        accessToken = runBlocking { encryptDeterministicallyInteractor(result.accessToken) }
                        expiryInMillis = result.expiryInMillis
                        accountManager.setAuthToken(account, AUTHTOKEN_TYPE, accessToken)
                        accountManager.setUserData(account, KEY_CUSTOM_TOKEN_EXPIRY, expiryInMillis.toString())
                    }
                    GetNewAccessTokenInteractor.Result.Failure.BadAuthentication ->  // Invalid refresh token
                        Timber.w("The refresh token is not valid")
                    GetNewAccessTokenInteractor.Result.Failure.NetworkError -> {
                        val error = "Network error while refreshing the token"
                        Timber.e(error)
                        errorBundle = getErrorBundle(AccountManager.ERROR_CODE_NETWORK_ERROR, error)
                    }
                }
            }
        }
        if (errorBundle != null) {
            return errorBundle
        }

        if (!accessToken.isNullOrEmpty()) {
            Timber.d("Returning a valid access token (expiry = ${expiryInMillis.toLongDateTimeString()}")
            return getValidAccessTokenBundle(account, accessToken, expiryInMillis)
        }

        // If we get here, it was not possible to get a new access token using the refresh token
        Timber.w("The user must re-enter the credentials")
        return bundleOf(AccountManager.KEY_INTENT to getAuthenticatorActivityIntent(response, account.type, options, false))
    }

    /**
     * Ask the authenticator for a localized label for the given authTokenType.
     *
     * @param authTokenType the authTokenType whose label is to be returned, will never be null
     * @return the localized label of the auth token type, may be null if the type isn't known
     */
    override fun getAuthTokenLabel(authTokenType: String): String? {
        Timber.d("getAuthTokenLabel()")
        return if (authTokenType == AUTHTOKEN_TYPE) authTokenType else null
    }

    /**
     * Update the locally stored credentials for an account.
     *
     * @param response to send the result back to the AccountManager, will never be null
     * @param account the account whose credentials are to be updated, will never be null
     * @param authTokenType the type of auth token to retrieve after updating the credentials,
     * may be null
     * @param options a Bundle of authenticator-specific options, may be null
     * @return a Bundle result or null if the result is to be returned via the response. The result
     * will contain either:
     * <ul>
     * <li> {@link AccountManager#KEY_INTENT}, or
     * <li> {@link AccountManager#KEY_ACCOUNT_NAME} and {@link AccountManager#KEY_ACCOUNT_TYPE} of
     * the account whose credentials were updated, or
     * <li> {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} to
     * indicate an error
     * </ul>
     * @throws NetworkErrorException if the authenticator could not honor the request due to a
     * network error
     */
    override fun updateCredentials(
        response: AccountAuthenticatorResponse,
        account: Account,
        authTokenType: String?,
        options: Bundle?
    ): Bundle? {
        Timber.d("updateCredentials()")
        return null
    }

    /**
     * Checks if the account supports all the specified authenticator specific features.
     *
     * @param response to send the result back to the AccountManager, will never be null
     * @param account the account to check, will never be null
     * @param features an array of features to check, will never be null
     * @return a Bundle result or null if the result is to be returned via the response. The result
     * will contain either:
     * <ul>
     * <li> {@link AccountManager#KEY_INTENT}, or
     * <li> {@link AccountManager#KEY_BOOLEAN_RESULT}, true if the account has all the features,
     * false otherwise
     * <li> {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} to
     * indicate an error
     * </ul>
     * @throws NetworkErrorException if the authenticator could not honor the request due to a
     * network error
     */
    override fun hasFeatures(
        response: AccountAuthenticatorResponse,
        account: Account,
        features: Array<out String>
    ): Bundle {
        Timber.d("hasFeatures()")
        // We do not support any additional features, so we return always false
        return bundleOf(AccountManager.KEY_BOOLEAN_RESULT to false)
    }

    private fun getAuthenticatorActivityIntent(
        response: AccountAuthenticatorResponse,
        accountType: String,
        options: Bundle,
        isNewAccount: Boolean,
    ) = (featureManager.getFeature(AccountFeature::class.java) as AccountFeature).mainActivityIntent.apply {
        putExtras(
            bundleOf(
                AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE to response,
                AccountManager.KEY_ACCOUNT_TYPE to accountType,
                AccountManager.KEY_USERDATA to options,
                KEY_IS_NEW_ACCOUNT to isNewAccount
            )
        )
    }

    private fun getAccountParameterMissingBundle(): Bundle {
        val error = "The provided account is null"
        Timber.e(error)
        return getErrorBundle(AccountManager.ERROR_CODE_BAD_ARGUMENTS, error)
    }

    private fun getAccountNotFoundBundle(): Bundle {
        val error = "The requested account does not exist on the device"
        Timber.e(error)
        return getErrorBundle(AccountManager.ERROR_CODE_BAD_ARGUMENTS, error)
    }

    private fun getWrongAuthTokenTypeBundle(authTokenType: String): Bundle {
        val error = "The auth token type $authTokenType is not valid"
        Timber.e(error)
        return getErrorBundle(AccountManager.ERROR_CODE_BAD_ARGUMENTS, error)
    }

    private fun getValidAccessTokenBundle(account: Account, accessToken: String, expiryInMillis: Long) = bundleOf(
        AccountManager.KEY_ACCOUNT_NAME to account.name,
        AccountManager.KEY_ACCOUNT_TYPE to account.type,
        AccountManager.KEY_AUTHTOKEN to accessToken,
        KEY_CUSTOM_TOKEN_EXPIRY to expiryInMillis
    )

    companion object {
        const val ACCOUNT_TYPE = BuildConfig.ACCOUNT_TYPE
        const val AUTHTOKEN_TYPE = "defaultAuthToken"  // https://stackoverflow.com/q/25056112/293878
        const val KEY_IS_NEW_ACCOUNT = "isNewAccount"
    }
}
