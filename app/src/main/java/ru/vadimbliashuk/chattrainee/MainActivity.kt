package ru.vadimbliashuk.chattrainee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var et_username: EditText
    private lateinit var et_email: EditText
    private lateinit var et_password: EditText
    private lateinit var btn_register: Button
    private lateinit var tv_already_have_an_account: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        et_username = findViewById(R.id.et_username_register)
        et_email = findViewById(R.id.et_email_register)
        et_password = findViewById(R.id.et_password_register)
        btn_register = findViewById(R.id.btn_register_register)
        tv_already_have_an_account = findViewById(R.id.tv_already_have_an_account)

        btn_register.setOnClickListener {
            Log.d("MainActivity", "Email is: ${et_email.text}")
            Log.d("MainActivity", "Password is: ${et_password.text}")

            // Firebase Authentication to create a user with email and password
        }

        tv_already_have_an_account.setOnClickListener {
            Log.d("MainActivity", "Try to show login activity")
                val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}
