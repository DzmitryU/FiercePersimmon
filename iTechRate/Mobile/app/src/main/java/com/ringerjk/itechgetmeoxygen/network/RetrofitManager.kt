package com.ringerjk.itechgetmeoxygen.network

import android.util.Log
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Yury Kanetski on 6/17/17.
 */
class RetrofitManager(val headerInterceptor: HeaderInterceptor) {

    private var retrofit: Retrofit

    init {
        val okClient = getClient()
        retrofit = Retrofit.Builder()
                .baseUrl("http://ec2-52-23-254-168.compute-1.amazonaws.com:3000/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okClient)
                .build()
    }

    private fun getClient(): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()

        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
            Log.d("RETROFIT_HTTP_LOGGING", message)
        })
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttpBuilder.addNetworkInterceptor(httpLoggingInterceptor)

        okHttpBuilder.addInterceptor(headerInterceptor)

        return okHttpBuilder.build()
    }

    fun getRetrofit(): Retrofit {
        return retrofit
    }
}