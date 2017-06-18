package com.ringerjk.itechgetmeoxygen.di

import com.ringerjk.itechgetmeoxygen.notifications.RefreshTokenService
import com.ringerjk.itechgetmeoxygen.ui.LogInActivity
import com.ringerjk.itechgetmeoxygen.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Yury Kanetski on 6/17/17.
 */

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

   fun plus(refreshTokenService: RefreshTokenService)

   fun plus(logInActivity: LogInActivity)

   fun plus(mainActivity: MainActivity)

}