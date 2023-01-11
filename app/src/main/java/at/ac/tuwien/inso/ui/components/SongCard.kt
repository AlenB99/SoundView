package at.ac.tuwien.inso.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.tuwien.inso.R
import at.ac.tuwien.inso.model.Song
import at.ac.tuwien.inso.ui.navigation.SoundViewScreens
import at.ac.tuwien.inso.ui.theme.AppTheme
import at.ac.tuwien.inso.ui.theme.md_theme_light_outlineVariant
import at.ac.tuwien.inso.ui.theme.md_theme_light_primaryContainer
import at.ac.tuwien.inso.ui.viewmodel.SongViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongCard(song: Song, navController: NavController, viewModel: SongViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        border = BorderStroke(2.dp, md_theme_light_outlineVariant),
        colors = CardDefaults.cardColors(containerColor = md_theme_light_primaryContainer),
        onClick = {
            viewModel.setSong(song)
            if (song.image_1.isNotEmpty()) {
                navController.navigate(route = SoundViewScreens.ImageChooserScreen.route)
            } else {
                navController.navigate(route = SoundViewScreens.LyricsKeywordsScreen.route)
            }
        }
    ) {
        Row {

            if (song.image_1.isNotEmpty()) {
                Column(modifier = Modifier.padding(8.dp)) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(song.image_1)
                            .crossfade(true)
                            .build(),
                        loading = {
                            CircularProgressIndicator()
                        },
                        contentDescription = stringResource(id = R.string.app_name),
                        modifier = Modifier.clip(RoundedCornerShape(10.dp))
                            .size(64.dp)

                    )
                }
            }
            Column(Modifier.padding(16.dp)) {

                Text(
                    text = song.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = song.artist,
                    // maxLines = 1,
                    // overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun PreviewSongCard() {
    AppTheme {
        SongCard(
            song = Song(
                id = "TEST",
                title = "The Fall",
                artist = "Eminem",
                image_1 = "test",
                image_2 = "test",
                image_3 = "test",
                image_4 = "test",
                keyPrompt = ""
            ),
            navController = rememberNavController(),
            viewModel = getViewModel()
        )
    }
}
