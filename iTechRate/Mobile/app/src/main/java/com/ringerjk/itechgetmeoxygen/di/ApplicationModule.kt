package com.ringerjk.itechgetmeoxygen.di

import android.content.Context
import com.ringerjk.itechgetmeoxygen.manager.NetworkManager
import com.ringerjk.itechgetmeoxygen.manager.SharedPreferenceManager
import com.ringerjk.itechgetmeoxygen.model.Api
import com.ringerjk.itechgetmeoxygen.network.HeaderInterceptor
import com.ringerjk.itechgetmeoxygen.network.RetrofitManager
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.Subject
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by Yury Kanetski on 6/17/17.
 */

@Module
class ApplicationModule(val appContext: Context) {

    @Provides
    @Singleton
    fun provideSharedPrefManager(): SharedPreferenceManager = SharedPreferenceManager(appContext)

    @Provides
    @Singleton
    fun provideToken(sharedPreferenceManager: SharedPreferenceManager): Subject<String> {
        return sharedPreferenceManager.serverTokenSubject
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(serverTokenSubject: Subject<String>?): HeaderInterceptor
            = HeaderInterceptor(serverTokenSubject)

    @Provides
    @Singleton
    fun provideRetrofit(headerInterceptor: HeaderInterceptor): Retrofit
            = RetrofitManager(headerInterceptor).getRetrofit()


    @Provides
    @Singleton
    fun provideNetworkManager(retrofit: Retrofit) : NetworkManager
            = NetworkManager(retrofit.create(Api::class.java))

}