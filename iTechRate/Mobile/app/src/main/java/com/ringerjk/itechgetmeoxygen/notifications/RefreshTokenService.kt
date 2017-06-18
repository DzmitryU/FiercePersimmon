package com.ringerjk.itechgetmeoxygen.notifications

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.ringerjk.itechgetmeoxygen.app.App
import com.ringerjk.itechgetmeoxygen.manager.NetworkManager
import com.ringerjk.itechgetmeoxygen.model.FirebaseToken
import com.ringerjk.itechgetmeoxygen.model.ResponceTemplate
import javax.inject.Inject

/**
 * Created by Yury Kanetski on 6/17/17.
 */

class RefreshTokenService : FirebaseInstanceIdService() {

    private val TAG = RefreshTokenService::class.java.simpleName
//    private var serverToken: String? = null

//    @Inject
//    lateinit var serverTokenSubject: Subject<String>

    @Inject
    lateinit var networkManager: NetworkManager

    init {
        App.Companion.applicationComponent.plus(this)
    }

    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: " + refreshedToken)

        sendRegistrationToServer(refreshedToken)
    }

    private fun sendRegistrationToServer(refreshedToken: String?){
        networkManager.updateFirebaseRefreshToken(FirebaseToken(refreshedToken))
                .subscribe({t: ResponceTemplate<Any>? ->
                    Log.d(TAG, "Update Firebase refresh token. Is ok? - "+t?.success)
                }, {t: Throwable? ->
                    Log.e(TAG, t?.message, t)
                })
    }
}