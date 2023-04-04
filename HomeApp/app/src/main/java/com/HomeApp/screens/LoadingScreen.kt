package com.HomeApp.screens

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.HomeApp.ui.navigation.Home
import com.HomeApp.ui.navigation.Login
import com.HomeApp.util.ApiConnector
import com.HomeApp.util.ApiResult
import com.HomeApp.util.HttpStatus
import com.HomeApp.util.LocalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoadingScreen(
    navController: NavController
) {
    // screen for loading while checking token
    // Backend respond, if we have an valid token
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var isAuth by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(true) }
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current

    val onAuth: (ApiResult) -> Unit = {
        isAuth = it.status() == HttpStatus.SUCCESS
        loading = false
        if (!isAuth) navController.navigate(Login.route)
        else navController.navigate(Home.route)
    }

    LaunchedEffect(navBackStackEntry) {
        launch {
            // Call backend to check if we already have an valid token
            coroutine.launch(Dispatchers.Main) {
                ApiConnector.getUserData(
                    token = LocalStorage.getToken(context),
                    onRespond = { onAuth(it) }
                )
            }
        }
    }


    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        val imageLoader = ImageLoader.Builder(context)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(context).data(data = com.HomeApp.R.drawable.gif_bg_transparent)
                    .apply(block = {
                        size(Size.ORIGINAL)
                    }).build(), imageLoader = imageLoader
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillHeight
        )
    }

}