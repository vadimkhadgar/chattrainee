package ru.vadimbliashuk.chattrainee

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import ru.vadimbliashuk.chattrainee.recyclerview.UserItem
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
            rv_newmessage.apply {
                layoutManager = LinearLayoutManager(this@NewMessageActivity)
                adapter = GroupAdapter<ViewHolder>().apply {
                    peopleSection = Section(items)
                    add(peopleSection)
                    setOnItemClickListener(onItemClick)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() {

        }

        if (shouldInitRecyclerView) {
            init()
        } else {
            updateItems()
        }
    }

    private val onItemClick = OnItemClickListener { item, _ ->
        if (item is UserItem) {
//            startActivity<ChatActivity>(
//                AppConstants.USER_NAME to item.person.name,
//                AppConstants.USER_ID to item.userId
//            )
            Toast.makeText(this, "Phew", Toast.LENGTH_LONG).show()
        }
    }
}
