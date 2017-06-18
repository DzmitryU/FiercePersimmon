package com.ringerjk.itechgetmeoxygen.manager

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

/**
 * Created by Yury Kanetski on 6/17/17.
 */
class SharedPreferenceManager(val appContext: Context) {
    val serverTokenSubject: Subject<String> = BehaviorSubject.create()

    private val EMPTY_TOKEN = ""
//    private val KEY_SP_FILE: String = BuildConfig.APPLICATION_ID + ".PREFERENCE_FILE_KEY"
    private val KEY_EXTRA_SERVER_TOKEN: String = "KEY_EXTRA_SERVER_TOKEN"

    init {
        notifyTokenUpdated(getServerToken())
    }

    fun saveServerToken(token: String) {
        putExtra(KEY_EXTRA_SERVER_TOKEN, token)
        notifyTokenUpdated(token)
    }

    fun getServerToken(): String? = getExtra(KEY_EXTRA_SERVER_TOKEN)

    fun removeServerToken() {
        removeExtra(KEY_EXTRA_SERVER_TOKEN)
        notifyTokenUpdated(null)
    }

    private fun notifyTokenUpdated(token: String?) {
        serverTokenSubject.onNext(token ?: EMPTY_TOKEN)
    }

    private fun getExtra(key: String): String? {
        return getSp().getString(key, null)
    }

    private fun putExtra(key: String, value: String) {
        getSp().edit().putString(key, value).apply()
    }

    private fun removeExtra(key: String) {
        getSp().edit().remove(key).apply()
    }

    private fun getSp(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext)


}