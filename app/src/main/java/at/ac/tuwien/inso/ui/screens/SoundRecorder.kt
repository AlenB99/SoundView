package at.ac.tuwien.inso.ui.screens

<<<<<<< Updated upstream
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
=======
import BottomNavBar
import android.Manifest
import android.media.MediaRecorder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*

>>>>>>> Stashed changes
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
<<<<<<< Updated upstream
=======
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
>>>>>>> Stashed changes
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.tuwien.inso.ui.navigation.SoundViewScreens
import at.ac.tuwien.inso.ui.theme.*
import at.ac.tuwien.inso.ui.viewmodel.GenerateCoverViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

<<<<<<< Updated upstream
@Composable
fun SoundRecorder(navController: NavController, viewModel: GenerateCoverViewModel) {
    AppTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = {
                    navController.navigate(route = SoundViewScreens.ImageGenerateDevToolScreen.route)
                          },
                // Uses ButtonDefaults.ContentPadding by default
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 12.dp,
                    end = 20.dp,
                    bottom = 12.dp
                )
            ) {
                // Inner content including an icon and a text label
                Text("Record Sound")
=======

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SoundRecorder(navController: NavController, viewModel: GenerateCoverViewModel) {
    var isRecording = false
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
            }, content = {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                        .background(md_theme_light_primary),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    Button(
                        modifier= Modifier.size(125.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = md_theme_light_primaryContainer,
                            contentColor = md_theme_light_scrim),
                        onClick = {
                            val file = File(context.filesDir.path, "/tmpaudio/")
                            MediaRecorder(context).apply {
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
                                        delay(10000) // Record for 10 seconds
                                        stop()
                                        release()
                                        val fileRead = File(fileName)
                                        println("!!!!!!!!!!!!!!!!!!!!")
                                        println(fileRead)
                                        val binaryData = fileRead.readBytes()
                                        println(binaryData)
                                        if (!Python.isStarted()) {
                                            Python.start(AndroidPlatform(context))
                                        }
                                        val py = Python.getInstance()
                                        val module = py.getModule("image_generate")
                                        try {
                                            val text = module.callAttr("scan_song", binaryData)
                                                .toString()
                                            println(text)
                                            val jsonObj = JSONObject(text).getJSONObject("result")
                                            val song = Song(
                                                id = 0,
                                                artist = jsonObj.get("artist").toString(),
                                                title = jsonObj.get("title").toString()
                                            )
                                            viewModel.setSong(song)
                                            println(song)
                                            isFinished = true
                                            // From python script we get a PyObject, which is converted to a string. Afterwards
                                            // its added to urlList, so that we can select the urls through indexing
                                        } catch (e: PyException) {
                                            println(e.message + " ")
                                        }
                                        isRecording = false
                                        println(isFinished)
                                    }
                                }


                            }

                        },
                        enabled = !isRecording
                    ) {
                        Text("R")
                    }
                }
            },bottomBar = {BottomNavBar(navController = navController)})

            if (isRecording) {
                Box(modifier = Modifier.padding(8.dp)) {
                    Text("Recording audio...")
                }
            }
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
>>>>>>> Stashed changes
            }

        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL_3A)
@Composable
fun PreviewSoundRecorder() {
    AppTheme {
        ImageGeneratorDevTool(
            navController = rememberNavController(),
            viewModel = getViewModel()
        )
    }
}


