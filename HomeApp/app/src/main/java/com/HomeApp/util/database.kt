package com.HomeApp.util

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

fun testDb(context: Context){
    val options = FirebaseOptions.Builder()
        .setDatabaseUrl(firebaseConfig["databaseURL"])
        .setProjectId(firebaseConfig["projectId"])
        .setApplicationId("1:217660507084:android:5ed9153722a55610c31369")
        .build()
    val initApp = FirebaseApp.initializeApp(context, options, "mobile_app")
    val database = FirebaseApp.getInstance("mobile_app")
    // Check for successful connection
    val connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected")
    connectedRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val connected = snapshot.getValue(Boolean::class.java) ?: false
            val TAG = "DATABASE"
            if (connected) {
                Log.d(TAG, "CONNTECTED SUCCESSFULLY")
            } else {
                Log.d(TAG, "DISCONNECTED SUCCESSFULLY")
            }
        }
        override fun onCancelled(error: DatabaseError) {
            println("Error checking connection status: $error")
        }
    })
//    val instance = FirebaseApp.getInstance()
//    print(initApp)

    val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
//    val path = "api/users/"
//    val request = Request.Builder()
//        .url(firebaseConfig["databaseURL"] + path)
//        .build()
//
//    client.newCall(request).enqueue(object: Callback{
//        val TAG = "DATABASE READ TEST: \t"
//        override fun onFailure(call: Call, e: IOException) {
////            println("Request failed: ${e.message}")
//            Log.d(TAG, "Fail")
//        }
//
//        override fun onResponse(call: Call, response: Response) {
//            val body = response.body?.string()
////            println("Response body: $body")
//            Log.d(TAG, "success")
//            Log.d("BODY", "$body")
//        }
//    })
}
