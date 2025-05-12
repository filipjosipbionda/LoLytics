package com.rma.lolytics

import android.app.Application
import com.rma.lolytics.data.di.dataModule
import com.rma.lolytics.ui.di.uiModule
import org.koin.core.context.startKoin

class LoLyticsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                dataModule,
                uiModule,
            )
        }
    }
}