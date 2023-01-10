package at.ac.tuwien.inso.ui.screens

import BottomNavBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.tuwien.inso.model.Song
import at.ac.tuwien.inso.ui.components.SongCard
import at.ac.tuwien.inso.ui.navigation.SoundViewScreens
import at.ac.tuwien.inso.ui.theme.AppTheme
import at.ac.tuwien.inso.ui.theme.md_theme_light_primaryContainer
import at.ac.tuwien.inso.ui.theme.md_theme_light_scrim
import at.ac.tuwien.inso.ui.viewmodel.SongViewModel
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.getViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LyricsKeywords(navController: NavController, viewModel: SongViewModel) {
    var lyrics by remember { mutableStateOf("Loading...") }
    var keywords by remember { mutableStateOf("") }
    var isFinished by remember { mutableStateOf(false) }
    var isManual by remember { mutableStateOf(false) }

    LaunchedEffect(isFinished) {
        if(isFinished) {
            navController.navigate(route = SoundViewScreens.ImageChooserScreen.route)
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Lyrics")
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
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val lyricsPrompt = (viewModel.song.value?.artist ?: "unknown") + " - " + (viewModel.song.value?.title ?: "unknown")
                if (!Python.isStarted()) {
                    Python.start(AndroidPlatform(LocalContext.current))
                }
                LaunchedEffect(Unit) {
                    CoroutineScope(Dispatchers.IO).launch {
                        withContext(Dispatchers.IO) {
                            lyrics = getLyrics(py = Python.getInstance(), prompt = lyricsPrompt)
                            keywords = applyNLP(py = Python.getInstance(), lyrics = lyrics)
                        }
                    }
                }

                Text(text = lyrics)
                //Text(text = keywords)

                val initialKeywords = keywords
                TextField(
                    value = keywords,
                    onValueChange = { newText ->
                        keywords = newText
                        if (keywords != initialKeywords){
                            isManual = true
                        }
                    }

                )
                if(isManual){
                    Button(
                        onClick = {
                            val song = Song(
                                id = UUID.randomUUID().toString(),
                                artist = "SoundViewUser",
                                title = keywords,
                                image_1 = "",
                                image_2 = "",
                                image_3 = "",
                                image_4 = "",
                                keyPrompt = "",
                            )
                            viewModel.setSong(song)
                            isFinished = true
                            //isManual = true

                        }
                    ) {
                        Text("Search1", color= Color.Black)
                    }

                }else{
                    Button(
                        onClick = {
                            val song = Song(
                                id = UUID.randomUUID().toString(),
                                artist = viewModel.song.value?.artist.toString(),
                                title = viewModel.song.value?.title.toString(),
                                image_1 = "",
                                image_2 = "",
                                image_3 = "",
                                image_4 = "",
                                keyPrompt = viewModel.song.value?.keyPrompt.toString(),
                            )
                            viewModel.setSong(song)
                            isFinished = true
                            //isManual = true

                        }
                    ) {
                        Text("Search2", color= Color.Black)
                    }
                }









            }
        },

        bottomBar = {BottomNavBar(navController = navController)})
}


@Preview(showBackground = true, device = Devices.PIXEL_3A)
@Composable
fun PreviewLK() {
    AppTheme {
        ImageGeneratorDevTool(
            navController = rememberNavController(),
            viewModel = getViewModel()
        )
    }
}




