package com.HomeApp.util

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.HomeApp.screens.Devices
import com.HomeApp.screens.Routines
import com.HomeApp.ui.composables.GroupsClass
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

fun <T> rememberFirestoreCollection(
    collectionPath: String,
    clazz: Class<T>,
    collectionType: String,
    userEmail: String = "",
): SnapshotStateList<DocumentSnapshot> {
    val email = userEmail.replace("\"", "")
    val collectionRef = if (collectionType == "groups" || collectionType == "routines") {
        FirebaseFirestore
            .getInstance()
            .collection("profiles")
            .document(email)
            .collection(collectionType)
    } else FirebaseFirestore.getInstance().collection(collectionPath)
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
    val devices = rememberFirestoreCollection("devices", Devices::class.java, "devices")
    val groups = rememberFirestoreCollection("", GroupsClass::class.java, "groups", userEmail = email)
    val routines = rememberFirestoreCollection("", Routines::class.java, "routines", userEmail = email)
}



