package com.leinardi.androidtemplateproject.initializer

import android.content.Context
import android.os.Build
import android.os.StrictMode
import androidx.startup.Initializer
import com.leinardi.androidtemplateproject.BuildConfig

class StrictModeInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            val builderThread = StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .permitDiskReads()
                .permitDiskWrites()
                .permitCustomSlowCalls()
                .penaltyLog()
                .penaltyDeath()
                .detectResourceMismatches()
            StrictMode.setThreadPolicy(builderThread.build())

            val builderVM = StrictMode.VmPolicy.Builder()
                .detectActivityLeaks()
                .detectFileUriExposure()
                .detectLeakedRegistrationObjects()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        detectContentUriWithoutPermission()
                    }
                }

            StrictMode.setVmPolicy(builderVM.build())
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
