package com.ringerjk.itechgetmeoxygen.network

import io.reactivex.subjects.Subject
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by Yury Kanetski on 6/17/17.
 */
class HeaderInterceptor (private val serverTokenSubject: Subject<String>?) : Interceptor {
    private var token: String? = null

    init {
        serverTokenSubject?.subscribe({ t: String? ->
            token = if (t.isNullOrEmpty()) null else t
        })
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("Content-Type", "application/json")

        if (token != null)
            requestBuilder.header("Authorization", token)

        val response = chain.proceed(requestBuilder.build())
        return response
    }
}