package com.HomeApp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.HomeApp.R
import com.HomeApp.ui.composables.RoutineCard
import com.HomeApp.ui.composables.RoutinesTitleBar
import com.HomeApp.ui.composables.RoutinesTitleBarItem
import com.HomeApp.ui.composables.TitledDivider
import com.HomeApp.util.DayFilters
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun <T> rememberFirestoreCollection(
    collectionPath: String,
    clazz: Class<T>,
    collectionType: String,
    userEmail: String = ""
): SnapshotStateList<DocumentSnapshot> {
    val collectionRef =
        if (collectionType == "groups" || collectionType == "routine") FirebaseFirestore.getInstance()
            .collection("profiles").document("raminkhareji@gmail.com").collection(collectionType)
        else FirebaseFirestore.getInstance().collection(collectionPath)
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

data class Routines(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val schedule: String = "",
    val enabled: Boolean = false,
    val repeatable: Boolean = false,
    val actionList: List<Action> = emptyList()
)

@Composable
fun RoutinesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    OnSelfClick: () -> Unit = {}
) {
    val coroutine = rememberCoroutineScope()
    val listHeight = LocalConfiguration.current.screenHeightDp
    val db = Firebase.firestore
    val documents = rememberFirestoreCollection("routines", Routines::class.java, "routine")

    Scaffold(
        topBar = {
            RoutinesTitleBar(item = RoutinesTitleBarItem.Routines, navController = navController)
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
                item { TitledDivider(navController = navController, title = "Filters") }
                items(items = documents, key = { item -> item.id }) { item ->
                    RoutineCard(navController = navController, RoutineItem = item)
                }
            }
        }
    )
}
