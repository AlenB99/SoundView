package at.ac.tuwien.inso.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.tuwien.inso.model.Song
import at.ac.tuwien.inso.ui.navigation.SoundViewScreens
import at.ac.tuwien.inso.ui.theme.AppTheme
import at.ac.tuwien.inso.ui.viewmodel.GenerateCoverViewModel
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.google.accompanist.permissions.*
import kotlinx.coroutines.*
import org.json.JSONObject
import org.koin.androidx.compose.getViewModel
import java.io.File
import java.io.IOException
import java.util.*
import java.util.Base64.getEncoder


@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SoundRecorder(navController: NavController, viewModel: GenerateCoverViewModel) {
    var isRecording = false
    var isFinished by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val allPermissionsState1 = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
        )
    )
    val allPermissionsState2 = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
        )
    )
    LaunchedEffect(isFinished) {
        if(isFinished) {
            navController.navigate(route = SoundViewScreens.ImageGenerateDevToolScreen.route)
        }
    }
    if (allPermissionsState1.allPermissionsGranted || allPermissionsState2.allPermissionsGranted)   {
        Text("Permission Granted")
        Button(
            onClick = {
                val file = File(context.filesDir.path, "/tmpaudio/")
                MediaRecorder(context).apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)

                    if (!file.exists()){
                        file.mkdirs();
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
            Text("Record Audio")
        }
        if (isRecording) {
            Box(modifier = Modifier.padding(8.dp)) {
                Text("Recording audio...")
            }
        }
    }
    else {
        Column {
            val allPermissionsRevoked1 =
                allPermissionsState1.permissions.size ==
                        allPermissionsState1.revokedPermissions.size
            val allPermissionsRevoked2 =
                allPermissionsState2.permissions.size ==
                        allPermissionsState2.revokedPermissions.size
            val textToShow = if (!allPermissionsRevoked1 || !allPermissionsRevoked2) {
                // If not all the permissions are revoked, it's because the user accepted the COARSE
                // location permission, but not the FINE one.
                "Yay! Thanks for letting me access your Audio and File System. "
            } else if (allPermissionsState1.shouldShowRationale || allPermissionsState2.shouldShowRationale) {
                // Both location permissions have been denied
                "Getting your Audio while you record is important for this app. " +
                        "Please grant us the Audio Record Permissions. Thank you :D"
            } else {
                // First time the user sees this feature or the user doesn't want to be asked again
                "This feature requires Audio and File permission"
            }

            val buttonText = if (!allPermissionsRevoked1 || !allPermissionsRevoked2) {
                "Allow Permissions"
            } else {
                "Request permissions"
            }

            Text(text = textToShow)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { allPermissionsState1.launchMultiplePermissionRequest()
                allPermissionsState2.launchMultiplePermissionRequest()

            }) {
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

