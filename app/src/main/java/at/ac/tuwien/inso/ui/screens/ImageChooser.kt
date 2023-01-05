package at.ac.tuwien.inso.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.tuwien.inso.R
import at.ac.tuwien.inso.ui.navigation.SoundViewScreens
import at.ac.tuwien.inso.ui.theme.AppTheme
import at.ac.tuwien.inso.ui.viewmodel.GenerateCoverViewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import kotlinx.coroutines.*
import org.koin.androidx.compose.getViewModel
import kotlinx.coroutines.launch



@Composable
fun ImageChooser(navController: NavController, prompt: String, viewModel: GenerateCoverViewModel) {
    if (!Python.isStarted()) {
        Python.start(AndroidPlatform(LocalContext.current))
    }

    val results = remember { mutableStateOf<List<String>?>(null) }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val data = withContext(Dispatchers.IO) {
                pythonScriptMain(py = Python.getInstance(), prompt = prompt)
            }
            results.value = data
        }
    }

    if (results.value == null) {
        CircularProgressIndicator(color = Color.Black)
    } else {
        viewModel.setImageurls(results.value!!)
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Images generated") },
                    navigationIcon = if (navController.previousBackStackEntry != null) {
                        {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    } else {
                        null
                    }

                )
            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

                    val imageModifier = Modifier
                        .size(150.dp)
                        .padding(5.dp)
                        .clip(RoundedCornerShape(25.dp))
                    Row{
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

                    Row{
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
                    Text(text = "Choose one to Download!", style = MaterialTheme.typography.caption)
                    SongTitle(name = "world")
                    Artist(name = "world")
                    Keywords(name = "world")
                }
            }
        )

    }




}



suspend fun pythonScriptMain(py: Python, prompt: String): List<String> {
    var urlList: List<String> = listOf("", "", "", "")
    val module = py.getModule("image_generate")
    try {
        val url = module.callAttr("image_generate", prompt)
            .toString()
        urlList = url.split(",").map {
            it.trim()
                .replace("[", "").replace("]", "")
                .replace("'", "")
        }
        return urlList;
        // From python script we get a PyObject, which is converted to a string. Afterwards
        // its added to urlList, so that we can select the urls through indexing
    } catch (e: PyException) {
        println(e.message + " ")
    }
    return urlList;
}

@Composable
    fun SongTitle(name: String) {
        Text(text = "Hello, $name!", style = MaterialTheme.typography.h3)
    }
    @Composable
    fun Artist(name: String) {
        Text(text = "Hello, $name!", style = MaterialTheme.typography.body1, textAlign = TextAlign.Start)
    }
    @Composable
    fun Keywords(name: String) {
        Text(text = "Hello, $name!", style = MaterialTheme.typography.caption)
    }

    @Preview(showBackground = true, device = Devices.PIXEL_3A)
    @Composable
    fun PreviewGreeting() {
        AppTheme {
            ImageChooser(
                navController = rememberNavController(),
                prompt = "test",
                viewModel = getViewModel()

            )
        }
    }