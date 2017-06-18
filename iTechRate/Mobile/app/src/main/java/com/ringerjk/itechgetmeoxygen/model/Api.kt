package com.ringerjk.itechgetmeoxygen.model

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by Yury Kanetski on 6/17/17.
 */
interface Api {

    @POST("login")
    fun login(@Body userCredentials: UserCredentials): Observable<ResponceTemplate<ServerAccessToken>>

    @POST("token")
    fun updateFirebaseRefreshToken(@Body token: FirebaseToken): Observable<ResponceTemplate<Any>>

    @GET("oxygen")
    fun getStateOfMyRoom(): Observable<ResponceTemplate<RoomState>>

    @GET("stat")
    fun getStateOfMyRoomFromPeriod(@Query("number") number: Int?): Observable<ResponceTemplate<RoomStates>>
}