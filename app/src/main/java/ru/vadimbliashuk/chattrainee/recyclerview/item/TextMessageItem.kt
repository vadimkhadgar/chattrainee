package ru.vadimbliashuk.chattrainee.recyclerview.item

import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_text_message.view.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.wrapContent
import ru.vadimbliashuk.chattrainee.R
import ru.vadimbliashuk.chattrainee.models.TextMessage
import java.text.SimpleDateFormat

class TextMessageItem(
    val message: TextMessage,
    val context: Context
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_message_text.text = message.text
        setTimeText(viewHolder)
        setMessageRootGravity(viewHolder)
    }

    private fun setTimeText(viewHolder: ViewHolder) {
        val dateFormat = SimpleDateFormat
            .getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)
        viewHolder.itemView.textView_message_time.text = dateFormat.format(message.time)
    }

    private fun setMessageRootGravity(viewHolder: ViewHolder) {
        if (message.senderId == FirebaseAuth.getInstance().currentUser?.uid) {
            viewHolder.itemView.message_root.apply {
                backgroundResource = R.drawable.rect_round_white
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.END)
                this.layoutParams = lParams
            }
        } else {
            viewHolder.itemView.message_root.apply {
                backgroundResource = R.drawable.rect_round_primary_color
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.START)
                this.layoutParams = lParams
            }
        }
    }


    override fun getLayout(): Int = R.layout.item_text_message

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is TextMessageItem)
            return false
        if (this.message != other.message)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? TextMessageItem)
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }
}
