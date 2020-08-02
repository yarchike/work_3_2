package com.yarchike.work_3_1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(isAuthenticated()){
            navigateToFeed()
            return
        }
        title = getString(R.string.title_authorization)
        btn_login.setOnClickListener {
            lifecycleScope.launch {
                val login = edt_login.text?.toString().orEmpty()
                val password = edt_password.text?.toString().orEmpty()
                val token = Repository.authenticate(login, password)

                if (token.isSuccessful) {
                    setUserAuth(requireNotNull(token.body()).toString())
                    navigateToFeed()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.authorisation_Error),
                        Toast.LENGTH_LONG
                    ).show()
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

    private fun isAuthenticated():Boolean =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getString(
            AUTHENTICATED_SHARED_KEY, ""
        )?.isNotEmpty() ?: false
    private fun setUserAuth(token: String) =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)
            .edit()
            .putString(AUTHENTICATED_SHARED_KEY, token)
            .apply()



}