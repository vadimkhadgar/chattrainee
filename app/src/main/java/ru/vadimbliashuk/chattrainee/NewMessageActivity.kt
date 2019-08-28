package ru.vadimbliashuk.chattrainee

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class NewMessageActivity : AppCompatActivity() {

    companion object {
        const val TAG = "New Message Activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"


    }


}
