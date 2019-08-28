package ru.vadimbliashuk.chattrainee

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var et_email_login: EditText
    private lateinit var et_password_login: EditText
    private lateinit var btn_login: Button
    private lateinit var tv_back_to_register: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        et_email_login = findViewById(R.id.et_email_login)
        et_password_login = findViewById(R.id.et_password_login)
        btn_login = findViewById(R.id.btn_login_login)
        tv_back_to_register = findViewById(R.id.tv_back_to_register_login)

        auth = FirebaseAuth.getInstance()

        btn_login.setOnClickListener {
            Log.d("Login", "Attempt login with email/pw: $et_email_login/***")
            doLogin()
        }

        tv_back_to_register.setOnClickListener {
            finish()
        }
    }


    private fun doLogin() {
        if (et_email_login.text.toString().isEmpty()) {
            et_email_login.error = "Please enter Email"
            et_email_login.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(et_email_login.text.toString()).matches()) {
            et_email_login.error = "Please enter valid Email"
            et_email_login.requestFocus()
            return
        }

        if (et_password_login.text.toString().isEmpty()) {
            et_password_login.error = "Please enter password"
            et_password_login.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(
            et_email_login.text.toString(),
            et_password_login.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                      updateUI(user)
                } else {
                    Toast.makeText(
                        baseContext, "Login failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    //   updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val intent = Intent(this, LatestMessagesActivity::class.java)
             startActivity(intent)
            finish()
        } else {
            Toast.makeText(
                baseContext, "Login failed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}