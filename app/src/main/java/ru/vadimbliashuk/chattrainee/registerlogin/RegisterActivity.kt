package ru.vadimbliashuk.chattrainee.registerlogin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import ru.vadimbliashuk.chattrainee.R
import ru.vadimbliashuk.chattrainee.messages.LatestMessagesActivity
import ru.vadimbliashuk.chattrainee.models.User
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var btn_select_photo_register: Button
    private lateinit var et_username_register: EditText
    private lateinit var et_email_register: EditText
    private lateinit var et_password_register: EditText
    private lateinit var btn_register: Button
    private lateinit var tv_already_have_an_account: TextView
    private lateinit var iv_select_photo_register: de.hdodenhof.circleimageview.CircleImageView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_select_photo_register = findViewById(R.id.btn_select_photo_register)
        et_username_register = findViewById(R.id.et_username_register)
        et_email_register = findViewById(R.id.et_email_register)
        et_password_register = findViewById(R.id.et_password_register)
        btn_register = findViewById(R.id.btn_register_register)
        tv_already_have_an_account = findViewById(R.id.tv_already_have_an_account)
        iv_select_photo_register = findViewById(R.id.iv_select_photo_register)

        auth = FirebaseAuth.getInstance()

        btn_register.setOnClickListener {
            // Firebase Authentication to create a user with email and password
            performRegister()

        }


        tv_already_have_an_account.setOnClickListener {
            Log.d("RegisterActivity", "Try to show login activity")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btn_select_photo_register.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("RegisterActivity", "Photo was selected")

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            iv_select_photo_register.setImageBitmap(bitmap)
            btn_select_photo_register.alpha = 0f
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
                    Log.d("RegisterActivity", "signInWithEmail:success")

                    uploadImageToFireBaseStorage()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("RegisterActivity", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun uploadImageToFireBaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity", "File location: $it")
                    saveUserToFirebaseFirestore(it.toString())
                }
            }
    }

    private fun saveUserToFirebaseFirestore(profileImageUrl: String) {
        val db = FirebaseFirestore.getInstance()

        val uid = FirebaseAuth.getInstance().uid ?: ""
        val user = User(uid, et_username_register.text.toString(), profileImageUrl)

        // Add a new document with a generated ID
        db.collection("users").document(uid)
            .set(user)
            .addOnSuccessListener {
                Log.d("SignUpActivity", "DocumentSnapshot successfully added")
                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Log.w("SignUpActivity", "Error adding document", e)
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
