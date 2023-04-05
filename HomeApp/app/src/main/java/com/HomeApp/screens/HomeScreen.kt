package com.HomeApp.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.HomeApp.R
import com.HomeApp.ui.composables.AppFooter
import com.HomeApp.ui.composables.TitledDivider
import com.HomeApp.ui.navigation.Groups
import com.HomeApp.ui.navigation.Routines
import com.HomeApp.ui.navigation.Settings
import com.HomeApp.ui.theme.GhostWhite
import com.HomeApp.ui.theme.montserrat
import com.HomeApp.util.microphoneIcon
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

fun <T> rememberFirestoreCollections(
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
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    OnSelfClick: () -> Unit = {},
    state: ScaffoldState
) {
    val spacerHeight: Dp = 112.dp
    Scaffold(
        content = {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
                    .padding(it)
                    .padding(horizontal = 5.dp)
                    .padding(vertical = 0.dp),
            ) {
                item {
                    MakeGroups(navController)
                }
                item { Spacer(modifier = Modifier.height(spacerHeight)) }
                item { MenuIcons(navController, state = state) }
                item { Spacer(modifier = Modifier.height(spacerHeight)) }
                item { TitledDivider(navController, "Activities", "Activities Divider") }
                item { Activities(navController) }
                item { Spacer(modifier = Modifier.height(56.dp)) }
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

@Composable
private fun MakeGroups(
    navController: NavController,
    modifier: Modifier = Modifier,
    scale: Float = 1f
) {
    Spacer(modifier = Modifier.height(28.dp))
    TitledDivider(navController, "Groups", "Groups Divider", showIcon = true)
    Column(
        modifier = modifier
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ) {
            val items = getDummyItems()
            itemsIndexed(items = items) { index, item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Group $index", color = if (MaterialTheme.colors.isLight) Color.Black else
                            Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    )
                    ClickableCard( // id should be passed along, as well.
                        onClick = { navController.navigate(Groups.route) },
                        url = item as String,
                        index = index,
                        scale = scale,
                        textCol = Color.Black
                    )
                }
            }
        }

    }
}

private fun getDummyItems(): List<Any> {
    val imageUrls = listOf(
        "https://picsum.photos/id/1025/4951/3301",
        "https://picsum.photos/id/1012/3973/2639",
        "https://picsum.photos/id/102/4320/3240",
        "https://picsum.photos/id/187/4000/2667",
        "https://picsum.photos/id/1020/4288/2848",
        "https://picsum.photos/id/1021/2048/1206",
        "https://picsum.photos/id/1024/1920/1280",
        "https://picsum.photos/id/1013/4256/2832"
    )
    return imageUrls
}


@Composable
fun ClickableCard(
    onClick: () -> Unit,
    url: String,
    scale: Float,
    index: Int,
    textCol: Color = Color.White,
    groupId: String = ""
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .padding(bottom = 15.dp)
            .size(112.dp)
            .fillMaxWidth()
            .scale(scale)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = 3.dp,
    ) {
        Image(
            painter = rememberAsyncImagePainter(url), contentDescription = null,
            modifier = Modifier
                .size(112.dp, 112.dp)
                .scale(1f),
            contentScale = ContentScale.FillBounds
        )
    }
}


@Composable
private fun MenuIcons(
    navController: NavController,
    modifier: Modifier = Modifier,
    scale: Float = 1f,
    state: ScaffoldState
) {
    val coroutine = rememberCoroutineScope()
    val screenWidth = LocalConfiguration.current.screenWidthDp
    // Routines
    Spacer(modifier = Modifier.width((screenWidth / 4).dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.padding(horizontal = 56.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        // Routines
        IconButton(
            onClick = { navController.navigate(Routines.route) }, modifier = Modifier
                .scale(1.5f)
                .background(Color.Gray, CircleShape)
        ) {
            Icon(
                imageVector = Routines.icon, contentDescription = "Routines Button",
                modifier = Modifier.scale(1.67f),
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Settings
        IconButton(
            onClick = {
                coroutine.launch {
                    if (state.drawerState.isClosed) state.drawerState.open()
                    else state.drawerState.close()
                }

            }, modifier = Modifier
                .scale(1.5f)
                .background(Color.Gray, CircleShape)
        ) {
            Icon(
                imageVector = Settings.icon, contentDescription = "Settings Button",
                modifier = Modifier.scale(1.6f),
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}


@Composable
private fun Activities(
    navController: NavController,
    modifier: Modifier = Modifier,
    scale: Float = 1f
) {

    val lightsOn = 1
    val iconsOn = 3

    val startIndex1 = 1
    val endIndex1 = startIndex1 + "$lightsOn".length
    val startIndex2 = "$iconsOn".length + 14
    val endIndex2 = startIndex2 + "$iconsOn".length

    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontFamily = montserrat)) {
            append(text = "$lightsOn")
        }
    }

    val annotatedText2 = buildAnnotatedString {
        withStyle(style = SpanStyle(fontFamily = montserrat)) {
            append(" lights on and ")
        }
    }

    val annotatedText3 = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontFamily = montserrat)) {
            append(text = "$iconsOn")
        }
    }

    val annotatedText4 = buildAnnotatedString {
        withStyle(style = SpanStyle(fontFamily = montserrat)) {
            append(" other devices currently running")
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { /*TODO*/ }, shape = RoundedCornerShape(10.dp),
            elevation = null,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.LightSteelBlue)
            ),
            modifier = Modifier
                .padding(horizontal = 40.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.LightMode,
                    contentDescription = "Active devices",
                    modifier = Modifier.padding(end = 10.dp)
                )
                Text(
                    text = annotatedText + annotatedText2 + annotatedText3 + annotatedText4,
                    modifier = Modifier.padding(top = 1.dp)
                )
            }
        }
    }

}
