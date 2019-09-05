package ru.vadimbliashuk.chattrainee.messages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import org.jetbrains.anko.startActivity
import ru.vadimbliashuk.chattrainee.AppConstants
import ru.vadimbliashuk.chattrainee.R
import ru.vadimbliashuk.chattrainee.recyclerview.item.UserItem
import ru.vadimbliashuk.chattrainee.util.FirestoreUtil


class NewMessageActivity : AppCompatActivity() {

    private lateinit var userListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true
    private lateinit var peopleSection: Section

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"

        userListenerRegistration =
            FirestoreUtil.addUserListener(this, this::updateRecyclerView)
    }

    override fun onDestroy() {
        super.onDestroy()
        FirestoreUtil.removeListener(userListenerRegistration)
        shouldInitRecyclerView = true
    }

    private fun updateRecyclerView(items: List<Item>) {

        fun init() {
            recyclerview_new_message.apply {
                layoutManager = LinearLayoutManager(this@NewMessageActivity)
                adapter = GroupAdapter<ViewHolder>().apply {
                    peopleSection = Section(items)
                    add(peopleSection)
                    setOnItemClickListener(onItemClick)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = peopleSection.update(items)

        if (shouldInitRecyclerView) {
            init()
        } else {
            updateItems()
        }
    }

    private val onItemClick = OnItemClickListener { item, view ->
        if (item is UserItem) {
            startActivity<ChatLogActivity>(
                AppConstants.USER_NAME to item.person.username,
                AppConstants.USER_ID to item.userId
            )
        }
    }
}
