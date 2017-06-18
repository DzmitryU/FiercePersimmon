package com.ringerjk.itechgetmeoxygen.manager

import com.ringerjk.itechgetmeoxygen.model.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Yury Kanetski on 6/17/17.
 */
class NetworkManager(private val api: Api) {

    private val TAG: String = NetworkManager::class.java.simpleName

    fun logIn(userCredentials: UserCredentials): Observable<ResponceTemplate<ServerAccessToken>>{
        return api.login(userCredentials)
                .subscribeOn(Schedulers.io())
    }

    fun updateFirebaseRefreshToken(firebaseToken: FirebaseToken): Observable<ResponceTemplate<Any>>{
        return api.updateFirebaseRefreshToken(firebaseToken)
                .subscribeOn(Schedulers.io())
    }

    fun getStateOfMyRoom(): Observable<ResponceTemplate<RoomState>>{
        return api.getStateOfMyRoom()
                .subscribeOn(Schedulers.io())
    }

    fun getStateOfMyRoomPeriod(number: Int?): Observable<ResponceTemplate<RoomStates>>{
        return api.getStateOfMyRoomFromPeriod(number)
                .subscribeOn(Schedulers.io())
    }
}
