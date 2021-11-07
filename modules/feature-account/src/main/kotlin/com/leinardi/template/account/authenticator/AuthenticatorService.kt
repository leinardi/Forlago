package com.leinardi.template.account.authenticator

import android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT
import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticatorService : Service() {
    @Inject lateinit var accountAuthenticator: AccountAuthenticator

    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate()")
    }

    override fun onBind(intent: Intent): IBinder? {
        Timber.d("onBind()")
        return if (intent.action == ACTION_AUTHENTICATOR_INTENT) accountAuthenticator.iBinder else null
    }
}
