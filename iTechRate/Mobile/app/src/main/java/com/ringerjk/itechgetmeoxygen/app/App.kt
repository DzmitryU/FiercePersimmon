package com.ringerjk.itechgetmeoxygen.app

import android.app.Application
import android.content.Context
import com.ringerjk.itechgetmeoxygen.di.ApplicationComponent
import com.ringerjk.itechgetmeoxygen.di.ApplicationModule
import com.ringerjk.itechgetmeoxygen.di.DaggerApplicationComponent

/**
 * Created by Yury Kanetski on 6/17/17.
 */
class App: Application() {

    companion object {
        private lateinit var appContext: Context

        val applicationComponent: ApplicationComponent by lazy {
            DaggerApplicationComponent.builder()
                    .applicationModule(ApplicationModule(appContext))
                    .build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext
    }
}