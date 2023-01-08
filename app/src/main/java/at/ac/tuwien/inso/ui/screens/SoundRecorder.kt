package at.ac.tuwien.inso.ui.screens

import BottomNavBar
import android.Manifest
import android.app.Dialog
import android.media.MediaRecorder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.rotate
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.tuwien.inso.R
import at.ac.tuwien.inso.model.Song
import at.ac.tuwien.inso.ui.components.SongCard
import at.ac.tuwien.inso.ui.navigation.SoundViewScreens
import at.ac.tuwien.inso.ui.theme.AppTheme
import at.ac.tuwien.inso.ui.theme.md_theme_light_primary
import at.ac.tuwien.inso.ui.theme.md_theme_light_primaryContainer
import at.ac.tuwien.inso.ui.theme.md_theme_light_scrim
import at.ac.tuwien.inso.ui.viewmodel.GenerateCoverViewModel
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.google.accompanist.permissions.*
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import org.koin.androidx.compose.getViewModel
import java.io.File
import java.io.IOException
import java.util.*


@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SoundRecorder(navController: NavController, viewModel: GenerateCoverViewModel) {
    var isRecording = false
    var notFound = false
    var isFinished by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val allPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    )
    LaunchedEffect(isFinished) {
        if(isFinished) {

            navController.navigate(route = SoundViewScreens.ImageGenerateDevToolScreen.route)
        }
    }
    if (allPermissionsState.allPermissionsGranted) {


        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Song Search")
                    },
                    navigationIcon = {

                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = md_theme_light_primaryContainer,
                        titleContentColor = md_theme_light_scrim,
                    )
                )
            }, content =
            {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                        .background(md_theme_light_primary),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    val showRecording = remember { mutableStateOf(false) }
                    val showFailed = remember { mutableStateOf(false) }
                    var currentRotation by remember { mutableStateOf(0f) }

                    val rotation = remember { Animatable(currentRotation) }

                    Button(
                        modifier= Modifier.size(125.dp)
                            .rotate(currentRotation),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = md_theme_light_primaryContainer,
                            contentColor = md_theme_light_scrim),
                        onClick = {
                            var mediaRecorderx= MediaRecorder()
                            val currentVersion = Build.VERSION.SDK_INT
                            if (currentVersion >= Build.VERSION_CODES.R) {
                                var mediaRecordex= MediaRecorder(context)
                            }
                            showRecording.value = true
                            showFailed.value = false
                            val file = File(context.filesDir.path, "/tmpaudio/")
                            mediaRecorderx.apply {
                                setAudioSource(MediaRecorder.AudioSource.MIC)
                                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)

                                if (!file.exists()) {
                                    file.mkdirs()
                                }
                                val fileName = file.toString() + "/" + Date() + ".ogg"
                                setOutputFile(fileName)
                                println(fileName)
                                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

                                try {
                                    prepare()
                                } catch (_: IOException) {

                                }

                                start()
                                isRecording = true
                                coroutineScope.launch {
                                    withContext(Dispatchers.IO) {
                                        coroutineScope.launch { rotation.animateTo(
                                            targetValue = currentRotation + 360f,
                                            animationSpec = infiniteRepeatable(
                                                animation = tween(3000, easing = LinearEasing),
                                                repeatMode = RepeatMode.Restart
                                            )
                                        ) {
                                            currentRotation = value
                                        }  }

                                        delay(10000) // Record for 10 seconds
                                        showRecording.value= false
                                        stop()
                                        release()
                                        val fileRead = File(fileName)
                                        val binaryData = fileRead.readBytes()
                                        if (!Python.isStarted()) {
                                            Python.start(AndroidPlatform(context))
                                        }
                                        val py = Python.getInstance()
                                        val module = py.getModule("image_generate")
                                        try {
                                            val text = module.callAttr("scan_song", binaryData)
                                                .toString()
                                            println(text)
                                            val textstring = "\"{”+ status” :”success”,”result”:null}\""
                                            println(text.length)

                                            val jsonObj = JSONObject(text).getJSONObject("result")
                                            println(jsonObj)
                                            val song = Song(
                                                id = 0,
                                                artist = jsonObj.get("artist").toString(),
                                                title = jsonObj.get("title").toString()
                                            )
                                            viewModel.setSong(song)
                                            isFinished = true



                                            // From python script we get a PyObject, which is converted to a string. Afterwards
                                            // its added to urlList, so that we can select the urls through indexing
                                        } catch (e: JSONException) {
                                            println(e.message + " ")
                                            showFailed.value= true
                                            println("Song not found ")
                                        }
                                        coroutineScope.launch { rotation.animateTo(
                                            targetValue = 1f,
                                            animationSpec = infiniteRepeatable(
                                                animation = tween(1, easing = LinearEasing)

                                            )

                                        )
                                        }
                                        isRecording = false
                                        currentRotation = 0f
                                    }
                                }


                            }

                        },
                        enabled = !isRecording
                    ) {
                        Icon(Icons.Rounded.MusicNote , contentDescription = "Localized description")
                    }
                    if (showRecording.value) {
                        Text("Searching...")
                    }
                    if (showFailed.value) {
                        Text("Could not find the song. Please try again.")
                    }

                }
            },
            bottomBar = {BottomNavBar(navController = navController)})


    }
    else {
        Column {
            val allPermissionsRevoked =
                allPermissionsState.permissions.size ==
                        allPermissionsState.revokedPermissions.size

            val textToShow = if (!allPermissionsRevoked) {
                // If not all the permissions are revoked, it's because the user accepted the COARSE
                // location permission, but not the FINE one.
                "Yay! Thanks for letting me access your Audio and File System. "
            } else if (allPermissionsState.shouldShowRationale) {
                // Both location permissions have been denied
                "Getting your Audio while you record is important for this app. " +
                        "Please grant us the Audio Record Permissions. Thank you :D"
            } else {
                // First time the user sees this feature or the user doesn't want to be asked again
                "This feature requires Audio and File permission"
            }

            val buttonText = if (!allPermissionsRevoked) {
                "Allow Permissions"
            } else {
                "Request permissions"
            }

        }
    }
}



@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun PreviewAudioRecorder() {
    AppTheme {
        SoundRecorder(
            navController = rememberNavController(),
            viewModel = getViewModel()
        )
    }
}

