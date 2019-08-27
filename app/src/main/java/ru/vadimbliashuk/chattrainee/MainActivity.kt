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

class MainActivity : AppCompatActivity() {

    private lateinit var et_username_register: EditText
    private lateinit var et_email_register: EditText
    private lateinit var et_password_register: EditText
    private lateinit var btn_register: Button
    private lateinit var tv_already_have_an_account: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        et_username_register = findViewById(R.id.et_username_register)
        et_email_register = findViewById(R.id.et_email_register)
        et_password_register = findViewById(R.id.et_password_register)
        btn_register = findViewById(R.id.btn_register_register)
        tv_already_have_an_account = findViewById(R.id.tv_already_have_an_account)

        auth = FirebaseAuth.getInstance()

        btn_register.setOnClickListener {
            Log.d("MainActivity", "Email is: ${et_email_register.text}")
            Log.d("MainActivity", "Password is: ${et_password_register.text}")

            // Firebase Authentication to create a user with email and password

            performRegister()

        }


        tv_already_have_an_account.setOnClickListener {
            Log.d("MainActivity", "Try to show login activity")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performRegister() {
        if (et_email_register.text.toString().isEmpty()) {
            et_email_register.error = "Please enter Email"
            et_email_register.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(et_email_register.text.toString()).matches()) {
            et_email_register.error = "Please enter valid Email"
            et_email_register.requestFocus()
            return
        }

        if (et_password_register.text.toString().isEmpty()) {
            et_password_register.error = "Please enter password"
            et_password_register.requestFocus()
            return
        }


        auth.createUserWithEmailAndPassword(
            et_email_register.text.toString(),
            et_password_register.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LoginActivity", "signInWithEmail:success")
                    val user = auth.currentUser
                    //     updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LoginActivity", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    //   updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            // startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        } else {
            Toast.makeText(
                baseContext, "Login failed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
