package com.example.kenangin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AuthPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_page)
        supportActionBar?.hide()
        val fragOne = LoginFragment()
        val fragTwo = RegisterFragment()
        supportFragmentManager.beginTransaction().apply {

            replace(R.id.fragment_main, fragOne)
            commit()
        }
        findViewById<Button>(R.id.fragmentLoginButton).setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_main, fragOne)
                commit()
            }
        }
        findViewById<Button>(R.id.fragmentRegisterButton).setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_main, fragTwo)
                commit()
            }
        }

    }


}