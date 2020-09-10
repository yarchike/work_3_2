package com.yarchike.work_3_1

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var dialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //requestToken()

        /*if (isAuthenticated()) {
            navigateToFeed()
            return
        }*/
        title = getString(R.string.title_authorization)
        btn_login.setOnClickListener {
            when {
                !isValidUsername(edt_login.text.toString()) -> {
                    til_login.error = getString(R.string.username_is_incorrect)
                }
                !isValidPassword(edt_password.text.toString()) -> {
                    til_password.error = getString(R.string.password_is_incorrect)
                }
                else -> {
                    lifecycleScope.launch {
                        dialog = ProgressDialog(this@MainActivity).apply {
                            setMessage(getString(R.string.please_wait))
                            setTitle(getString(R.string.loading_data))
                            show()
                            setCancelable(false)
                        }


                        val login = edt_login.text?.toString().orEmpty()
                        val password = edt_password.text?.toString().orEmpty()
                        try {
                            val token = App.repository.authenticate(login, password)

                            dialog?.dismiss()
                            if (token.isSuccessful) {
                                setUserAuth(requireNotNull(token.body()?.token))
                                requestToken()
                                navigateToFeed()
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    getString(R.string.authorisation_Error),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.falien_connect),
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog?.dismiss()
                        }
                    }
                }
            }
        }
        btn_registration.setOnClickListener {
            navigateToRegistration()
        }
    }

    private fun navigateToRegistration() {
        val intent = Intent(this@MainActivity, RegistrationActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToFeed() {
        val intent = Intent(this@MainActivity, FeedActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isAuthenticated(): Boolean =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getString(
            AUTHENTICATED_SHARED_KEY, ""
        )?.isNotEmpty() ?: false

    private fun setUserAuth(token: String) =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)
            .edit()
            .putString(AUTHENTICATED_SHARED_KEY, token)
            .apply()

    private fun requestToken() {
        with(GoogleApiAvailability.getInstance()) {
            val code = isGooglePlayServicesAvailable(this@MainActivity)
            if (code == ConnectionResult.SUCCESS) {
                return@with
            }

            if (isUserResolvableError(code)) {
                getErrorDialog(this@MainActivity, code, 9000).show()
                return
            }

            root.longSnackbar(getString(R.string.google_play_unavailable))
            return
        }

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            lifecycleScope.launch {
                println(it.token)

                App.repository.registerPushToken(it.token)
            }
        }
    }


}