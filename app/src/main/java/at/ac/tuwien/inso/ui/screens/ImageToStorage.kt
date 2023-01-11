package at.ac.tuwien.inso.ui.screens

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.tuwien.inso.R
import at.ac.tuwien.inso.ui.components.BottomNavBar
import at.ac.tuwien.inso.ui.theme.AppTheme
import at.ac.tuwien.inso.ui.theme.md_theme_light_primaryContainer
import at.ac.tuwien.inso.ui.theme.md_theme_light_scrim
import at.ac.tuwien.inso.ui.viewmodel.SongViewModel
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.getViewModel
import java.net.URL
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ImageToStorage(navController: NavController, viewModel: SongViewModel) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.title_cover_art))
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

                Image(
                    painter = rememberAsyncImagePainter(viewModel.imageurl.value),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = imageModifier
                )

                SongTitle(name = viewModel.song.value!!.title)
                Artist(name = viewModel.song.value!!.artist)
                Keywords(name = viewModel.song.value!!.keyPrompt)

                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            val url = URL(viewModel.imageurl.value)
                            val connection =
                                withContext(Dispatchers.IO) {
                                    url.openConnection()
                                }
                            val inputStream =
                                withContext(Dispatchers.IO) {
                                    connection.getInputStream()
                                }
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            val values = ContentValues().apply {
                                put(
                                    MediaStore.MediaColumns.DISPLAY_NAME,
                                    "SoundView_" + viewModel.song
                                        .value!!.title + "_" + Date() + ".png"
                                )
                                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                            }
                            val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                            uri?.let {
                                context.contentResolver.openOutputStream(it).use { outputStream ->
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                                }
                            }
                        }
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
                    Text("Download")
                }
            }
        },
        bottomBar = { BottomNavBar(navController = navController) }
    )
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true, device = Devices.PIXEL_3A)
@Composable
fun PreviewItS() {
    AppTheme {
        ImageToStorage(
            navController = rememberNavController(),
            viewModel = getViewModel()
        )
    }
}
