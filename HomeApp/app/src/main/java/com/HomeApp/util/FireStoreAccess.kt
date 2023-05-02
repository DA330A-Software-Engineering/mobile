package com.HomeApp.util

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

fun rememberFirestoreCollection(
    collectionPath: String,
    collectionType: String,
    userEmail: String,
): SnapshotStateList<DocumentSnapshot> {
    val email = userEmail.replace("\"", "")
    val documents = mutableStateListOf<DocumentSnapshot>()  //mutableStateOf(MutableList<T>())
    if (email != "") {
        val collectionRef =
            if (collectionType == "groups" || collectionType == "routines" || collectionType == "triggers") {
                FirebaseFirestore
                    .getInstance()
                    .collection("profiles")
                    .document(email)
                    .collection(collectionType)
            } else {
                FirebaseFirestore.getInstance().collection(collectionPath)
            }
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
            }

        }
    }
    var counter = 0
    return documents
}

/**fun getDocument(collectionPath: String, documentPath: String): DocumentSnapshot? {
    Log.d("I'm HERRRRRREEEEE", "collection path: $collectionPath documentpath: $documentPath")
    var document: DocumentSnapshot? = null
    val documentRef = FirebaseFirestore.getInstance().collection(collectionPath).document(documentPath)


    documentRef.get().addOnSuccessListener { documentSnapshot ->
            Log.d("OK OK OK HERE", documentSnapshot.id)
            document = documentSnapshot
    }

    return document
}*/

fun getTrigger(context: Context, triggerId: String, callback: (document: DocumentSnapshot?) -> Unit) {
    val email = getEmailFromToken(context)
    val userEmail = email.replace("\"", "")
    val documentRef = FirebaseFirestore
        .getInstance()
        .collection("profiles")
        .document(userEmail)
        .collection("triggers")
        .document(triggerId)

    documentRef.get().addOnSuccessListener { documentSnapShot ->
        callback(documentSnapShot)
    }.addOnFailureListener {
        callback(null)
    }
}

fun getDocument(collectionPath: String, documentPath: String, callback: (document: DocumentSnapshot?) -> Unit) {
    val documentRef = FirebaseFirestore.getInstance().collection(collectionPath).document(documentPath)

    documentRef.get().addOnSuccessListener { documentSnapshot ->
        callback(documentSnapshot)
    }.addOnFailureListener {
        callback(null)
    }
}

class RealTimeData(context: Context) {
    var email = getEmailFromToken(context)
    val devices = rememberFirestoreCollection(
        collectionPath = "devices",
        collectionType = "devices",
        userEmail = email
    )
    val groups = rememberFirestoreCollection(
        collectionPath = "",
        collectionType = "groups",
        userEmail = email
    )
    val routines = rememberFirestoreCollection(
        collectionPath = "",
        collectionType = "routines",
        userEmail = email
    )
    val triggers = rememberFirestoreCollection(
        collectionPath = "",
        collectionType = "triggers",
        userEmail = email
    )
}
