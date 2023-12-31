package at.ac.tuwien.inso.ui.screens

import at.ac.tuwien.inso.ui.components.BottomNavBar
import android.Manifest
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.tuwien.inso.R
import at.ac.tuwien.inso.model.Song
import at.ac.tuwien.inso.ui.navigation.SoundViewScreens
import at.ac.tuwien.inso.ui.theme.AppTheme
import at.ac.tuwien.inso.ui.theme.md_theme_light_primary
import at.ac.tuwien.inso.ui.theme.md_theme_light_primaryContainer
import at.ac.tuwien.inso.ui.theme.md_theme_light_scrim
import at.ac.tuwien.inso.ui.viewmodel.SongViewModel
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
import java.util.UUID.randomUUID

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SoundRecorder(navController: NavController, viewModel: SongViewModel) {
    var isRecording by remember { mutableStateOf(false) }
    var isFinished by remember { mutableStateOf(false) }
    val isManual by remember { mutableStateOf(false) }
    var error = ""
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val currentVersion = Build.VERSION.SDK_INT

    // If android API is above 30, there is a bug where storage permissions are not asked.
    // See issue thread for example: https://github.com/xamarin/Essentials/issues/2041
    val allPermissionsState = if (currentVersion >= Build.VERSION_CODES.R) {
        rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.RECORD_AUDIO
            )
        )
    } else {
        rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )
    }
    LaunchedEffect(isFinished) {
        if (isFinished) {

            navController.navigate(route = SoundViewScreens.ImageGenerateDevToolScreen.route)
        }
    }
    LaunchedEffect(isManual) {
        if (isManual) {

            navController.navigate(route = SoundViewScreens.ImageChooserScreen.route)
        }
    }
    if (allPermissionsState.allPermissionsGranted) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(R.string.title_song_search))
                    },
                    navigationIcon = {
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = md_theme_light_primaryContainer,
                        titleContentColor = md_theme_light_scrim,
                    )
                )
            },
            content =
            {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                        .background(md_theme_light_primary),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var currentRotation by remember { mutableStateOf(0f) }

                    val rotation = remember { Animatable(currentRotation) }
                    LaunchedEffect(isRecording) {

                        if (isRecording) {
                            rotation.animateTo(
                                targetValue = currentRotation + 360f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(3000, easing = LinearEasing),
                                    repeatMode = RepeatMode.Restart
                                )
                            ) {
                                currentRotation = value
                            }
                        } else {
                            currentRotation = 0f
                        }
                    }
                    Button(
                        modifier = Modifier
                            .size(125.dp)
                            .rotate(currentRotation),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = md_theme_light_primaryContainer,
                            contentColor = md_theme_light_scrim
                        ),
                        onClick = {
                            error = ""
                            var mediaRecorderx = MediaRecorder()
                            if (currentVersion >= Build.VERSION_CODES.S) {
                                mediaRecorderx = MediaRecorder(context)
                            }
                            val file = File(context.filesDir.path, "/tmpaudio/")
                            mediaRecorderx.apply {
                                setAudioSource(MediaRecorder.AudioSource.MIC)
                                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)

                                if (!file.exists()) {
                                    file.mkdirs()
                                }
                                val fileName = file.toString() + "/" + Date() + ".ogg"
                                setOutputFile(fileName)
                                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

                                try {
                                    prepare()
                                } catch (_: IOException) {
                                }

                                start()
                                isRecording = true

                                coroutineScope.launch {
                                    withContext(Dispatchers.IO) {

                                        delay(10000) // Record for 10 seconds
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
                                            val jsonObj = JSONObject(text).getJSONObject("result")
                                            val song = Song(
                                                id = randomUUID().toString(),
                                                artist = jsonObj.get("artist").toString(),
                                                title = jsonObj.get("title").toString(),
                                                image_1 = "",
                                                image_2 = "",
                                                image_3 = "",
                                                image_4 = "",
                                                keyPrompt = "",
                                            )
                                            viewModel.insertSong(song)

                                            viewModel.setSong(song)
                                            isFinished = true

                                            // From python script we get a PyObject, which is converted to a string. Afterwards
                                            // its added to urlList, so that we can select the urls through indexing
                                        } catch (e: JSONException) {
                                            error = "Could not find the song. Please try again."
                                            currentRotation = 0f
                                        } catch (e: PyException) {
                                            error = "Network Error!"
                                            currentRotation = 0f
                                        }
                                        isRecording = false
                                        currentRotation = 0f
                                    }
                                }
                            }
                        },
                        enabled = !isRecording
                    ) {
                        Icon(Icons.Rounded.MusicNote, contentDescription = "Localized description")
                    }
                    Spacer(modifier = Modifier.size(32.dp))
                    if (isRecording) {
                        Text("Searching...", color = Color.White)
                    }

                    Text(error, color = Color.White)
                }
            },
            bottomBar = { BottomNavBar(navController = navController) }
        )
    } else {
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
            Text(text = textToShow)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { allPermissionsState.launchMultiplePermissionRequest() }) {
                Text(buttonText)
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
