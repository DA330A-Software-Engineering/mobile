package com.HomeApp

import android.Manifest.permission.RECORD_AUDIO
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import com.HomeApp.drawers.SideDrawer
import com.HomeApp.ui.navigation.AnimatedAppNavHost
import com.HomeApp.ui.navigation.Home
import com.HomeApp.ui.navigation.Loading
import com.HomeApp.ui.theme.GhostWhite
import com.HomeApp.ui.theme.HomeAppTheme
import com.HomeApp.util.*
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.text.Regex
import java.util.*

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onResume() {
        super.onResume()
        window.decorView.windowInsetsController!!.hide(android.view.WindowInsets.Type.navigationBars())
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onStart() {
        super.onStart()
        window.decorView.windowInsetsController!!.hide(android.view.WindowInsets.Type.navigationBars())
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val onRespond: (ApiResult) -> Unit = {
                Log.d("RESPOND", it.toString())
                val coroutine = CoroutineScope(Dispatchers.Main)
                val data: JSONObject? = try {
                    it.data()
                } catch (e: JSONException) {
                    null
                }
                val msg: String = data?.get("msg").toString()
                val context = this.baseContext
                Log.d("ACTION", "onRespond got called with object $it")
                coroutine.launch {
                    when (it.status()) {
                        HttpStatus.SUCCESS -> {
                            Toast.makeText(
                                context,
                                "Successfully updated your device or group",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("ACTION", "msg: $msg | Action successful")
                        }
                        HttpStatus.UNAUTHORIZED -> {
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                            Log.d("ACTION", "$data")
                        }
                        HttpStatus.FAILED -> {
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                            Log.d("ACTION", "$data")
                        }
                    }
                }
            }

            val actionCall: (
                context: Context,
                jsonObj: JSONObject,
                documentSnapShot: DocumentSnapshot
            ) -> Unit = { context, jsonObj, it ->
                val coroutine = CoroutineScope(Dispatchers.Main)
                coroutine.launch(Dispatchers.IO) {
                    if (jsonObj.length() == 1) {
                        ApiConnector.deviceAction(
                            token = LocalStorage.getToken(context),
                            id = it.id,
                            state = jsonObj,
                            type = it.get("type") as String,
                            onRespond = onRespond
                        )
                    }
                }
            }

            if (result.resultCode == Activity.RESULT_OK) {
                val data: ArrayList<String> =
                    result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                        ?: return@registerForActivityResult
                val input = data[0] // grabs String from ArrayList
                val TAG = "ACTION"
                Log.d(TAG, "MIC: $input")
                FirebaseFirestore.getInstance().collection("devices").get()
                    .addOnSuccessListener { it ->
                        it.documents.forEach {
                            if (input.lowercase()
                                    .contains((it.get("name") as String).lowercase())
                            ) {
                                val inputOptions = input.split("and")
                                val context = this.baseContext
                                var primaryAction: Boolean? = null
                                var secondaryAction: Any? = null
                                val it = it


                                inputOptions.forEach { str ->
                                    if (str.lowercase()
                                            .contains((it.get("name") as String).lowercase())
                                    ) {
                                        when (it.get("type")) {
                                            "toggle" -> {
                                                primaryAction = when {
                                                    str.lowercase().contains("on") -> true
                                                    str.lowercase().contains("off") -> false
                                                    else -> null
                                                }
                                                val jsonObj = JSONObject()
                                                if (primaryAction != null) {
                                                    jsonObj.put("on", primaryAction)
                                                }
                                                Log.d(
                                                    "ACTION",
                                                    "obj = $jsonObj \naction=$primaryAction\nobjLength: ${jsonObj.length()}"
                                                )
                                                actionCall(context, jsonObj, it)
                                            }
                                            "openLock" -> {
                                                primaryAction = when {
                                                    str.lowercase().contains("open") -> true
                                                    str.lowercase().contains("close") -> false
                                                    else -> null
                                                }
                                                secondaryAction = when {
                                                    str.lowercase().contains("unlock") -> false
                                                    str.lowercase().contains("lock") -> true
                                                    else -> null
                                                }
                                                val jsonObj = JSONObject()
                                                if (primaryAction != null) {
                                                    jsonObj.put("open", primaryAction)
                                                }
                                                if (secondaryAction != null) {
                                                    jsonObj.put("locked", secondaryAction)
                                                }
                                                actionCall(context, jsonObj, it)
                                            }
                                            "fan" -> {
                                                val isReversed: Boolean =
                                                    (it.get("state") as Map<String, Boolean>)["reverse"] as Boolean
                                                primaryAction = when {
                                                    str.lowercase().contains("on") -> true
                                                    str.lowercase().contains("off") -> false
                                                    else -> null
                                                }
                                                secondaryAction = when {
                                                    str.contains("reverse") -> !isReversed
                                                    else -> null
                                                }
                                                val jsonObj = JSONObject()
                                                if (primaryAction != null) {
                                                    jsonObj.put("on", primaryAction)
                                                }
                                                if (secondaryAction != null) {
                                                    jsonObj.put("reverse", secondaryAction)
                                                }
                                                actionCall(context, jsonObj, it)
                                            }
                                            "screen" -> {
                                                val re = "\\b(write|right|ride|rite|rice)\\b".toRegex()
                                                val delimiter = str.split("\\b(to|on)\\b".toRegex())
                                                val triggerWordFound = delimiter[0].contains(re)
                                                var value = ""
                                                if (triggerWordFound){
                                                    // "Write arabic nights on the screen" -> "arabic nights"
                                                    value = delimiter[0].lowercase().split(re)[1].trim()
                                                    value = value.substring(0, min(15, value.length))
                                                }
                                                primaryAction = when {
                                                    str.lowercase().contains("write") -> true
                                                    str.lowercase().contains("right") -> true // incase of mispronounciation
                                                    str.lowercase().contains("rides") -> true // incase of mispronounciation
                                                    str.lowercase().contains("rice") -> true // incase of mispronounciation
                                                    str.lowercase().contains("set") -> true
                                                    else -> null
                                                }
                                                val jsonObj = JSONObject()
                                                if (primaryAction != null && value.isNotEmpty()) {
                                                    jsonObj.put("text", value)
                                                }
                                                Log.d(
                                                    "ACTION",
                                                    "obj = $jsonObj \nval=$value\nobjLength: ${jsonObj.length()}"
                                                )
                                                actionCall(context, jsonObj, it)
                                            }
                                            "buzzer" -> {
                                                val tunes = listOf("pokemon", "pirates")
                                                primaryAction = when {
                                                    str.lowercase().contains("play") -> true
                                                    else -> null
                                                }
                                                val song = when {
                                                    str.lowercase().contains("alarm") -> "alarm"
                                                    str.lowercase().contains("pirate") -> "pirate"
                                                    else -> null
                                                }
                                                val jsonObj = JSONObject()
                                                if (primaryAction != null && song != null) {
                                                    jsonObj.put("tune", song)
                                                    actionCall(context, jsonObj, it)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
            }
        }

    // on below line we are creating a method
    // to get the speech input from user.
    private fun getSpeechInput(context: Context) {
        // on below line we are checking if speech
        // recognizer intent is present or not.
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            // if the intent is not present we are simply displaying a toast message.
            Toast.makeText(context, "Speech not Available", Toast.LENGTH_SHORT).show()
        } else {
            // on below line we are calling a speech recognizer intent
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            // on the below line we are specifying language model as language web search
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
            )

            // on below line we are specifying extra language as default english language
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

            // on below line we are specifying prompt as Speak something
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Something")

            // at last we are calling start activity
            // for result to start our activity.
            resultLauncher.launch(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = GhostWhite
                ) {
                    ActivityCompat.requestPermissions(this, arrayOf(RECORD_AUDIO), 0)
                    RunApp(getSpeechInput = { getSpeechInput(it) })
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RunApp(getSpeechInput: (Context) -> Unit = {}) {
    val context = LocalContext.current
    FirebaseApp.initializeApp(context)
    val navController = rememberAnimatedNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val state = rememberScaffoldState(
        rememberDrawerState(initialValue = DrawerValue.Closed)
    )
    val coroutine = rememberCoroutineScope()
    var isAuth by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(true) }


    val onAuth: (ApiResult) -> Unit = {
        isAuth = (it.status() == HttpStatus.SUCCESS)
        loading = false
    }
    val token = LocalStorage.getToken(context)
    LaunchedEffect(navBackStackEntry) {
        launch {
            // Call backend to check if we already have an valid token
            coroutine.launch(Dispatchers.IO) {
                ApiConnector.getAllUserData(
                    token = LocalStorage.getToken(context),
                    onRespond = { onAuth(it) }
                )
            }
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            scaffoldState = state,
            topBar = {
                if (enableTopDrawer) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp)
                                .background(Color.LightGray)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "Top bar content",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            },
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Box(
                        modifier = Modifier
                            .padding(it) // only applied when there is a bottomBar added.
                    ) {
                        AnimatedAppNavHost(
                            navController = navController,
                            startDestination = Loading.route,
                            state = state,
                            getSpeechInput = { getSpeechInput(it) }
                        )
                    }
                }
            },
            drawerShape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp),
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr)
                {
                    val sideDrawer: SideDrawer = SideDrawer(
                        drawerState = state.drawerState,
                        navController = navController
                    )
                    sideDrawer.drawScaffold()
                }
            },
            drawerGesturesEnabled = state.drawerState.isOpen
        )
    }
}
