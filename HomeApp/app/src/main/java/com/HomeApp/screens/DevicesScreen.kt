package com.HomeApp.screens

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.DeviceCard
import com.HomeApp.ui.composables.FilteredList
import com.HomeApp.ui.composables.TitleBar
import com.HomeApp.ui.composables.TitledDivider
import com.HomeApp.ui.navigation.NavPath
import com.HomeApp.util.DevicesFilters
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Objects

data class Devices (
    var id: String = "",
    var type: String = "",
    var name: String = "",
    var description: String = "",
    var state: Map<String, Any> = emptyMap(),
    var available: Boolean = true
)

fun <T> rememberFirestoreCollection(collectionPath: String, clazz: Class<T>): State<List<T>> {
    val collectionRef = FirebaseFirestore.getInstance().collection(collectionPath)
    val documents = mutableStateOf(emptyList<T>())

    collectionRef.addSnapshotListener { snapshot, error ->
        if (error != null) {
            // Handle the error
            return@addSnapshotListener
        }
        if (snapshot != null) {
            documents.value = snapshot.toObjects(clazz)
        }
        //Log.d(TAG, "Look here $documents")
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
    //var filtersSelected by remember { mutab }
    val coroutine = rememberCoroutineScope()
    val listHeight = LocalConfiguration.current.screenHeightDp
    val db = Firebase.firestore
    val documents by rememberFirestoreCollection("devices", Devices::class.java)

    Scaffold(
        topBar = {

            TitleBar(screenTitle = "Devices", navController = navController)
        },
        content = {
            Log.d(TAG, listHeight.toString())
            Column(modifier = Modifier
                .padding(it)
                .fillMaxHeight()) {
                TitledDivider(navController = navController, title = "Filters")
                FilteredList(filterScreen="devices")

                LazyColumn(
                    modifier = Modifier
                        .height(listHeight.dp)
                        .padding(vertical = 10.dp)
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(items = documents, key = { item -> item.name }) { item ->
                        DeviceCard(navController = navController, deviceItem = item)
                    }
                }
            }
        },
        bottomBar = {

        }
    )
}