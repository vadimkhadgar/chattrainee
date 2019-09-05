package ru.vadimbliashuk.chattrainee.messages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import ru.vadimbliashuk.chattrainee.AppConstants
import ru.vadimbliashuk.chattrainee.R
import ru.vadimbliashuk.chattrainee.models.MessageType
import ru.vadimbliashuk.chattrainee.models.TextMessage
import ru.vadimbliashuk.chattrainee.util.FirestoreUtil
import java.util.*

class ChatLogActivity : AppCompatActivity() {

    private lateinit var messageListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true
    private lateinit var messagesSection: Section

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        supportActionBar?.title = intent.getStringExtra(AppConstants.USER_NAME)

        val otherUserId = intent.getStringExtra(AppConstants.USER_ID)

        FirestoreUtil.getOrCreateChatChannel(otherUserId) { channelId ->
            messageListenerRegistration =
                FirestoreUtil.addChatMessageListener(channelId, this, this::updateRecyclerView)

            btn_send_chat_log.setOnClickListener {
                val messageToSend =
                    TextMessage(
                        et_enter_message_chat_log.text.toString(), Calendar.getInstance().time,
                        FirebaseAuth.getInstance().currentUser!!.uid, otherUserId, MessageType.TEXT
                    )
                et_enter_message_chat_log.setText("")
                FirestoreUtil.sendMessage(messageToSend, channelId)
            }

            image_btn_send_image_chat_log.setOnClickListener {
                //TODO: Send Image Messages
            }
        }
    }

    private fun updateRecyclerView(messages: List<Item>) {
        fun init() {
            recyclerview_chat_log.apply {
                layoutManager = LinearLayoutManager(this@ChatLogActivity)
                adapter = GroupAdapter<ViewHolder>().apply {
                    messagesSection = Section(messages)
                    this.add(messagesSection)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = messagesSection.update(messages)

        if (shouldInitRecyclerView)
            init()
        else
            updateItems()

        recyclerview_chat_log.scrollToPosition(recyclerview_chat_log.adapter!!.itemCount - 1)
    }
}
