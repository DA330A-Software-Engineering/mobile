package com.HomeApp.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.*
import com.HomeApp.ui.theme.GhostWhite
import com.HomeApp.util.microphoneIcon
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


data class Devices(
    var id: String = "",
    var type: String = "",
    var name: String = "",
    var description: String = "",
    var state: Map<String, Any> = emptyMap(), // This should be <String, Boolean> once database structure is updated
    var available: Boolean = true
)

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


@Composable
fun DevicesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    state: ScaffoldState,
    OnSelfClick: () -> Unit = {}
) {
    val coroutine = rememberCoroutineScope()
    val listHeight = LocalConfiguration.current.screenHeightDp
    val db = Firebase.firestore
    val documents = rememberFirestoreCollection("devices", Devices::class.java)
    var value = ""

    TextField(value = value, onValueChange = {value = it})

    Scaffold(
        topBar = {
            TitleBar(screenTitle = "Devices", navController = navController, isDevices = true)
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .height(listHeight.dp)
                    .padding(it)
                    .padding(vertical = 10.dp)
                    .padding(top = 10.dp)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items = documents, key = { item -> item.id }) { item ->
                    DeviceCard(navController = navController, deviceItem = item)
                }
            }
        },
        bottomBar = {
            AppFooter(
                navController = navController,
                bgCol = MaterialTheme.colors.background,
                micColor = if (MaterialTheme.colors.isLight) Color.Black else Color.White
            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                backgroundColor = GhostWhite,
                modifier = Modifier.scale(1f)
            ) {
                Icon(
                    tint = Color.Black,
                    imageVector = microphoneIcon,
                    contentDescription = "Mic",
                    modifier = Modifier.scale(1.4f)
                )
            }
        }, isFloatingActionButtonDocked = true, floatingActionButtonPosition = FabPosition.Center
    )
}
