package at.ac.tuwien.inso.ui.screens

import android.media.MediaRecorder
import android.os.Environment
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.tuwien.inso.ui.navigation.SoundViewScreens
import at.ac.tuwien.inso.ui.theme.AppTheme
import at.ac.tuwien.inso.ui.viewmodel.GenerateCoverViewModel
import org.koin.androidx.compose.getViewModel

private var mediaRecorder: MediaRecorder? = null
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
                    var savepath = (Environment.getExternalStorageDirectory().absolutePath
                            + "/" + "recordingAudio.mp3")
                    var mediaRecorder = MediaRecorder(LocalContext.current)
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    mediaRecorder.setOutputFile(savepath)


                    mediaRecorder.prepare()
                    mediaRecorder.start()


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


