package at.ac.tuwien.inso.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.tuwien.inso.R
import at.ac.tuwien.inso.model.Song
import at.ac.tuwien.inso.ui.navigation.SoundViewScreens
import at.ac.tuwien.inso.ui.theme.AppTheme
import at.ac.tuwien.inso.ui.theme.md_theme_light_primaryContainer
import at.ac.tuwien.inso.ui.theme.md_theme_light_scrim
import at.ac.tuwien.inso.ui.viewmodel.SongViewModel
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
    var isDone by remember { mutableStateOf(false) }

    LaunchedEffect(isFinished) {
        if (isFinished) {
            navController.navigate(route = SoundViewScreens.ImageChooserScreen.route)
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    androidx.compose.material3.Text(text = stringResource(R.string.title_lyrics_keywords))
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = md_theme_light_primaryContainer,
                    titleContentColor = md_theme_light_scrim,
                ),
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }

            )
        },
        content =
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .verticalScroll(rememberScrollState())
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
                            lyrics = viewModel.getLyrics(py = Python.getInstance(), prompt = lyricsPrompt)
                            keywords = viewModel.applyNLP(py = Python.getInstance(), lyrics = lyrics)
                            viewModel.setKeywords(keywords)
                        }
                        isDone = true
                    }
                }
                if (!isDone) {
                    androidx.compose.material.CircularProgressIndicator(color = Color.Black)
                }
                androidx.compose.material3.Text(
                    text = viewModel.song.value!!.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                androidx.compose.material3.Text(
                    text = viewModel.song.value!!.artist,
                    // maxLines = 1,
                    // overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(modifier = Modifier.height(16.dp))
                androidx.compose.material3.Text(text = lyrics)
            }
        },

        bottomBar = {
            Row {
                val initialKeywords = keywords
                TextField(
                    value = keywords,
                    onValueChange = { newText ->
                        keywords = newText
                        if (keywords != initialKeywords) {
                            isManual = true
                        }
                    }

                )
                if (isManual) {
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
                            // isManual = true
                        }
                    ) {
                        Text("Generate", color = Color.White)
                    }
                } else {
                    Button(
                        onClick = {
                            Song(
                                id = UUID.randomUUID().toString(),
                                artist = viewModel.song.value?.artist.toString(),
                                title = viewModel.song.value?.title.toString(),
                                image_1 = "",
                                image_2 = "",
                                image_3 = "",
                                image_4 = "",
                                keyPrompt = viewModel.keywords.value.toString(),
                            )
                            viewModel.updateSong(emptyList(), viewModel.keywords.value.toString())

                            isFinished = true
                        }
                    ) {
                        Text("Generate", color = Color.White)
                    }
                }
            }
        }
    )
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
