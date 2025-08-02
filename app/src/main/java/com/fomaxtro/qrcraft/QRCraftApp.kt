package com.fomaxtro.qrcraft

import android.app.Application
import com.fomaxtro.core.data.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class QRCraftApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@QRCraftApp)
            androidLogger()

            modules(
                dataModule
            )
        }
    }
}