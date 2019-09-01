package ru.vadimbliashuk.chattrainee.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.vadimbliashuk.chattrainee.R

class ChatLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        supportActionBar?.title = "Chat Log"


    }
}
