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
import at.ac.tuwien.inso.ui.theme.AppTheme
import at.ac.tuwien.inso.ui.viewmodel.GenerateCoverViewModel
import com.google.accompanist.permissions.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.getViewModel
import java.io.File
import java.io.IOException
import java.util.Date


@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SoundRecorder(navController: NavController, viewModel: GenerateCoverViewModel) {
    var isRecording = false
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val allPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    )
    if (allPermissionsState.allPermissionsGranted) {
        Text("Permission Granted")
        Button(
            onClick = {
                val file = File(context.filesDir.path, "/tmpaudio/")
                MediaRecorder(context).apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)

                    println("BACK ON "+ file.exists())
                    if (!file.exists()){
                        file.mkdirs();
                    }

                    setOutputFile(file.toString() + "/" + Date() + ".ogg")
                    println(file.toString() + "/" + Date() + ".ogg")
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
                            isRecording = false
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
            val allPermissionsRevoked =
                allPermissionsState.permissions.size ==
                        allPermissionsState.revokedPermissions.size

            val textToShow = if (!allPermissionsRevoked) {
                // If not all the permissions are revoked, it's because the user accepted the COARSE
                // location permission, but not the FINE one.
                "Yay! Thanks for letting me access your approximate location. " +
                        "But you know what would be great? If you allow me to know where you " +
                        "exactly are. Thank you!"
            } else if (allPermissionsState.shouldShowRationale) {
                // Both location permissions have been denied
                "Getting your exact location is important for this app. " +
                        "Please grant us fine location. Thank you :D"
            } else {
                // First time the user sees this feature or the user doesn't want to be asked again
                "This feature requires location permission"
            }

            val buttonText = if (!allPermissionsRevoked) {
                "Allow precise location"
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

