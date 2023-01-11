package at.ac.tuwien.inso.ui.screens

import at.ac.tuwien.inso.ui.components.BottomNavBar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import at.ac.tuwien.inso.R
import at.ac.tuwien.inso.ui.navigation.SoundViewScreens
import at.ac.tuwien.inso.ui.theme.md_theme_light_primaryContainer
import at.ac.tuwien.inso.ui.theme.md_theme_light_scrim
import at.ac.tuwien.inso.ui.viewmodel.SongViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import kotlinx.coroutines.*
import kotlinx.coroutines.launch

@Composable
fun ImageChooser(navController: NavController, viewModel: SongViewModel) {
    if (viewModel.song.value!!.image_1.isEmpty()) {
        ImageGenerator(
            navController = navController, prompt = viewModel.song.value!!.keyPrompt,
            viewModel = viewModel
        )
    } else {
        ImageDisplayer(
            navController = navController, viewModel = viewModel
        )
    }
}


@Composable
fun SongTitle(name: String) {
    Text(text = name, style = MaterialTheme.typography.titleLarge)
}
@Composable
fun Artist(name: String) {
    Text(text = name, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Start)
}
@Composable
fun Keywords(name: String) {
    Text(text = name, style = MaterialTheme.typography.labelMedium)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageGenerator(navController: NavController, prompt: String, viewModel: SongViewModel) {
    if (!Python.isStarted()) {
        Python.start(AndroidPlatform(LocalContext.current))
    }

    val results = remember { mutableStateOf<List<String>?>(null) }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val data = withContext(Dispatchers.IO) {
                viewModel.pythonScriptMain(py = Python.getInstance(), prompt = prompt)
            }
            results.value = data
            viewModel.updateSong(data, prompt)
        }
    }

    if (results.value == null) {
        CircularProgressIndicator(color = Color.Black)
    } else {
        viewModel.setImageurls(results.value!!)
        viewModel.updateSong(results.value!!, prompt)
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(R.string.title_images_generated))
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
            content = { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    val imageModifier = Modifier
                        .size(150.dp)
                        .padding(5.dp)
                        .clip(RoundedCornerShape(25.dp))
                    Row {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(results.value!![0])
                                .crossfade(true)
                                .build(),
                            loading = {
                                CircularProgressIndicator()
                            },
                            contentDescription = stringResource(id = R.string.app_name),
                            modifier = imageModifier
                                .clickable(onClick = {
                                    viewModel.setImageurl(0)
                                    navController.navigate(route = SoundViewScreens.ImageToStorageScreen.route)
                                })

                        )

                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(results.value!![1])
                                .crossfade(true)
                                .build(),
                            loading = {
                                CircularProgressIndicator()
                            },
                            contentDescription = stringResource(id = R.string.app_name),
                            modifier = imageModifier
                                .clickable(onClick = {
                                    viewModel.setImageurl(1)
                                    navController.navigate(route = SoundViewScreens.ImageToStorageScreen.route)
                                })

                        )
                    }

                    Row {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(results.value!![2])
                                .crossfade(true)
                                .build(),
                            loading = {
                                CircularProgressIndicator()
                            },
                            contentDescription = stringResource(id = R.string.app_name),
                            modifier = imageModifier
                                .clickable(onClick = {
                                    viewModel.setImageurl(2)
                                    navController.navigate(route = SoundViewScreens.ImageToStorageScreen.route)
                                })

                        )

                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(results.value!![3])
                                .crossfade(true)
                                .build(),
                            loading = {
                                CircularProgressIndicator()
                            },
                            contentDescription = stringResource(id = R.string.app_name),
                            modifier = imageModifier
                                .clickable(onClick = {
                                    viewModel.setImageurl(3)
                                    navController.navigate(route = SoundViewScreens.ImageToStorageScreen.route)
                                })

                        )
                    }
                    Text(text = stringResource(R.string.caption_download), style = MaterialTheme.typography.labelMedium)
                    SongTitle(name = viewModel.song.value!!.title)
                    Artist(name = viewModel.song.value!!.artist)
                    Keywords(name = stringResource(R.string.caption_keywords) + viewModel.song.value!!.keyPrompt)
                }
            },
            bottomBar = { BottomNavBar(navController = navController) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDisplayer(navController: NavController, viewModel: SongViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.title_images_generated)) },
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
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val imageModifier = Modifier
                    .size(150.dp)
                    .padding(5.dp)
                    .clip(RoundedCornerShape(25.dp))
                Row {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(viewModel.song.value!!.image_1)
                            .crossfade(true)
                            .build(),
                        loading = {
                            CircularProgressIndicator()
                        },
                        contentDescription = stringResource(id = R.string.app_name),
                        modifier = imageModifier
                            .clickable(onClick = {
                                viewModel.setImageurl(0)
                                navController.navigate(route = SoundViewScreens.ImageToStorageScreen.route)
                            })

                    )

                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(viewModel.song.value!!.image_2)
                            .crossfade(true)
                            .build(),
                        loading = {
                            CircularProgressIndicator()
                        },
                        contentDescription = stringResource(id = R.string.app_name),
                        modifier = imageModifier
                            .clickable(onClick = {
                                viewModel.setImageurl(1)
                                navController.navigate(route = SoundViewScreens.ImageToStorageScreen.route)
                            })

                    )
                }

                Row {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(viewModel.song.value!!.image_3)
                            .crossfade(true)
                            .build(),
                        loading = {
                            CircularProgressIndicator()
                        },
                        contentDescription = stringResource(id = R.string.app_name),
                        modifier = imageModifier
                            .clickable(onClick = {
                                viewModel.setImageurl(2)
                                navController.navigate(route = SoundViewScreens.ImageToStorageScreen.route)
                            })

                    )

                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(viewModel.song.value!!.image_4)
                            .crossfade(true)
                            .build(),
                        loading = {
                            CircularProgressIndicator()
                        },
                        contentDescription = stringResource(id = R.string.app_name),
                        modifier = imageModifier
                            .clickable(onClick = {
                                viewModel.setImageurl(3)
                                navController.navigate(route = SoundViewScreens.ImageToStorageScreen.route)
                            })

                    )
                }
                Text(text = stringResource(R.string.caption_download), style = MaterialTheme.typography.labelMedium)
                SongTitle(name = viewModel.song.value!!.title)
                Artist(name = viewModel.song.value!!.artist)
                Keywords(name = stringResource(R.string.caption_keywords) + viewModel.song.value!!.keyPrompt)
            }
        },
        bottomBar = { BottomNavBar(navController = navController) }
    )
}
