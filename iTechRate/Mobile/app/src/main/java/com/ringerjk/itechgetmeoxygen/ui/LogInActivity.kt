package com.ringerjk.itechgetmeoxygen.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.iid.FirebaseInstanceId
import com.ringerjk.itechgetmeoxygen.R
import com.ringerjk.itechgetmeoxygen.app.App
import com.ringerjk.itechgetmeoxygen.manager.NetworkManager
import com.ringerjk.itechgetmeoxygen.manager.SharedPreferenceManager
import com.ringerjk.itechgetmeoxygen.model.FirebaseToken
import com.ringerjk.itechgetmeoxygen.model.ResponceTemplate
import com.ringerjk.itechgetmeoxygen.model.ServerAccessToken
import com.ringerjk.itechgetmeoxygen.model.UserCredentials
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class LogInActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = LogInActivity::class.java.simpleName

    @Inject
    lateinit var networkManager: NetworkManager
    @Inject
    lateinit var spManager: SharedPreferenceManager

    private var loginEt: EditText? = null
    private var passwordEt: EditText? = null
    private var logInBtn: Button? = null
    private var progressDialog: MaterialDialog? = null

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        App.Companion.applicationComponent.plus(this)
        if (spManager.getServerToken() != null)
            startMainActivity()

        initView()

    }

    private fun initView() {
        loginEt = findViewById(R.id.login_et_login_activity) as EditText?
        loginEt?.setText("yury.kanetski")
        passwordEt = findViewById(R.id.password_et_login_activity) as EditText?
        passwordEt?.setText("123")
        logInBtn = findViewById(R.id.log_in_btn) as Button?
        logInBtn?.setOnClickListener(this)

        progressDialog = MaterialDialog.Builder(this)
                .progress(true, 0).build()
    }

    override fun onClick(v: View?) {
        when (v) {
            logInBtn -> {
                progressDialog?.show()
                networkManager.logIn(UserCredentials(loginEt?.text.toString(),
                        passwordEt?.text.toString()))
                        .map { t: ResponceTemplate<ServerAccessToken>? ->
                            t?.body
                        }.map { t: ServerAccessToken? ->
                    val token = t?.access_token
                    if (token != null) spManager.saveServerToken(token) else spManager.removeServerToken()
                    return@map token
                }.observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ t: String? ->
                            Log.d(TAG, "Server Token $t")
                            networkManager
                                    .updateFirebaseRefreshToken(FirebaseToken(FirebaseInstanceId.getInstance().token))
                                    .subscribe()
                            startMainActivity()
                            progressDialog?.dismiss()
                        }, { t: Throwable? ->
                            Log.e(TAG, t?.message, t)
                            Toast.makeText(this,
                                    "Server is not available now. Please try late",
                                    Toast.LENGTH_SHORT
                            ).show()
                            progressDialog?.dismiss()
                        })
            }
        }
    }

    private fun startMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
