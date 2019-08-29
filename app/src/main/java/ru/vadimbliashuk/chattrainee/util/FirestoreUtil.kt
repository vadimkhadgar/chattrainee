package ru.vadimbliashuk.chattrainee.util

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.xwray.groupie.kotlinandroidextensions.Item
import ru.vadimbliashuk.chattrainee.models.User
import ru.vadimbliashuk.chattrainee.recyclerview.UserItem

object FirestoreUtil {
    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    // private val currentUserDocRef: DocumentReference

    fun addUserListener(context: Context, onListen: (List<Item>) -> Unit): ListenerRegistration {
        return firestoreInstance.collection("users")
            .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "User Listener Error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<Item>()
                querySnapshot!!.documents.forEach {
                    if (it.id != FirebaseAuth.getInstance().currentUser?.uid) {
                        items.add(UserItem(it.toObject(User::class.java)!!, it.id, context))
                    }
                }
                onListen(items)


            }
    }

    fun removeListener(registration: ListenerRegistration) = registration.remove()
}