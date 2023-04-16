package com.HomeApp.util

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

fun <T> rememberFirestoreCollection(
    collectionPath: String,
    clazz: Class<T>
): SnapshotStateList<DocumentSnapshot> {
    val collectionRef = FirebaseFirestore.getInstance().collection(collectionPath)
    val documents = mutableStateListOf<DocumentSnapshot>()  //mutableStateOf(MutableList<T>())
    var counter = 0

    collectionRef.addSnapshotListener { snapshot, error ->
        if (error != null) {
            // Handle the error
            return@addSnapshotListener
        }
        if (snapshot != null) {
            documents.clear()
            snapshot.documents.forEach { item ->
                documents.add(item)
            }

            //Log.d(TAG, "Look here ${snapshot.documents}")
            //Log.d(TAG, "Look here 2 ${documents}")
        }

    }
    return documents
}