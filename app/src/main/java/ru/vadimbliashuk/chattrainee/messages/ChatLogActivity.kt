package ru.vadimbliashuk.chattrainee.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item
import ru.vadimbliashuk.chattrainee.AppConstants
import ru.vadimbliashuk.chattrainee.R
import ru.vadimbliashuk.chattrainee.util.FirestoreUtil

class ChatLogActivity : AppCompatActivity() {

    private lateinit var messageListenerRegistration: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        supportActionBar?.title = intent.getStringExtra(AppConstants.USER_NAME)

        val otherUserId = intent.getStringExtra(AppConstants.USER_ID)
        FirestoreUtil.getOrCreateChatChannel(otherUserId) { channelId ->
            messageListenerRegistration =
                FirestoreUtil.addChatMessageListener(channelId, this, this::onMessageChanged)


        }
    }

    private fun onMessageChanged(messages: List<Item>) {
       Toast.makeText(this, "OnMessagesChangedRunning", Toast.LENGTH_LONG).show()
    }
}
