package com.HomeApp.drawers

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.HomeApp.ui.composables.TitledDivider
import com.HomeApp.ui.theme.montserrat
import com.HomeApp.util.SideBarOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

class SideDrawer(
    private val drawerState: DrawerState,
    private val navController: NavController,
    private val modifier: Modifier = Modifier
) {


    @SuppressLint("ComposableNaming")
    @Composable
    fun drawScaffold() {
        val coroutine = rememberCoroutineScope()

        BackHandler(enabled = true) {
            coroutine.launch {
                drawerState.close()
            }
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray),
            topBar = { drawHead(modifier = modifier.scale(1.75f)) },
            content = {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        drawBody(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it)
                        )
                    }
                }
            },
            bottomBar = { drawFooter() }
        )
    }

    @SuppressLint("ComposableNaming")
    @Composable
    private fun drawHead(modifier: Modifier = Modifier) {
        val coroutine = rememberCoroutineScope()
        val primaryCol = Color.Black
        val primaryAlpha: Float = 1f
        val bgCol = Color.Transparent

        Column {
            Image(
                painter = rememberAsyncImagePainter("https://images.desenio.com/zoom/10801_2.jpg"),
                contentDescription = "Head image",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight(0.3f)
            )
            Row(
                modifier = Modifier
                    .background(bgCol)
                    .padding(bottom = 20.dp)
            ) {
                Spacer(modifier = Modifier.width(12.dp)) // cover scrim area
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                ) {
                    TitledDivider(
                        navController = navController, title = "Settings", modifier =
                        Modifier.scale(1f), fontSize = 25
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }


    @SuppressLint("ComposableNaming")
    @Composable
    private fun drawBody(modifier: Modifier = Modifier) {
        val coroutine: CoroutineScope = rememberCoroutineScope()
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier)
        ) {
            SideBarOptions.values().forEach {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 15.dp)
                ) {
                    Button(
                        onClick = { navController.navigate(it.route) }, elevation = null,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(0.dp),
                        modifier = Modifier.fillMaxHeight(),
                    ) {
                        Text(
                            text = it.title, style = TextStyle(
                                fontFamily = montserrat,
                                fontWeight = FontWeight.W400
                            )
                        )
                        Spacer(Modifier.weight(1f))
                        if (it.icon != null) {
                            Icon(imageVector = it.icon, contentDescription = null)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
            }
            Spacer(modifier = Modifier.weight(2f))
        }
    }


    @SuppressLint("ComposableNaming")
    @Composable
    private fun drawFooter(modifier: Modifier = Modifier) {
        val build = "v1.3.2 (ac9defgh)"
        Column(
            modifier = Modifier
                .background(Color.DarkGray)
                .height(
                    max(LocalConfiguration.current.screenHeightDp.dp / 16, 57.dp)
                )
                .then(modifier)
        ) {
            Spacer(Modifier.height(12.dp))
            Spacer(Modifier.weight(1f))
            Text(
                AnnotatedString("App version: $build"),
                color = Color.LightGray,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.5f)
                    .padding(start = 25.dp),
                textAlign = TextAlign.Left, fontSize = 12.sp
            )
            Spacer(Modifier.weight(1f))
        }
    }
}
