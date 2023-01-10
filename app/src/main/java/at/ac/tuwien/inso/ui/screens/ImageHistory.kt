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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.tuwien.inso.model.Song
import at.ac.tuwien.inso.ui.components.SongCard
import at.ac.tuwien.inso.ui.theme.AppTheme
import at.ac.tuwien.inso.ui.theme.md_theme_light_primaryContainer
import at.ac.tuwien.inso.ui.theme.md_theme_light_scrim
import at.ac.tuwien.inso.ui.viewmodel.SongViewModel
import org.koin.androidx.compose.getViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageHistory(navController: NavController, viewModel: SongViewModel, songs: List<Song>) {


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Generated Image History")
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (item in songs) {
                    if(item.image_1.isNotEmpty()){
                        item{
                            SongCard(song = item , navController = navController, viewModel = viewModel)
                            Spacer(modifier = Modifier.size(16.dp))
                        }
                    }

                }
            }
        },
        bottomBar = {BottomNavBar(navController = navController)})
}


@Preview(showBackground = true, device = Devices.PIXEL_3A)
@Composable
fun PreviewIH() {
    AppTheme {
        ImageGeneratorDevTool(
            navController = rememberNavController(),
            viewModel = getViewModel()
        )
    }
}


