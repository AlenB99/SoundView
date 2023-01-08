package at.ac.tuwien.inso.ui.screens

import BottomNavBar
import androidx.compose.foundation.layout.*
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
import at.ac.tuwien.inso.ui.components.SongCard
import at.ac.tuwien.inso.ui.theme.AppTheme
import at.ac.tuwien.inso.ui.theme.md_theme_light_primaryContainer
import at.ac.tuwien.inso.ui.theme.md_theme_light_scrim
import at.ac.tuwien.inso.ui.viewmodel.SongViewModel
import org.koin.androidx.compose.getViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageGeneratorDevTool(navController: NavController, viewModel: SongViewModel) {


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Song found!")
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
                    .padding(it),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
               Button(
                    modifier = Modifier.size(125.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = md_theme_light_primaryContainer,
                        contentColor = md_theme_light_scrim
                    ),
                    onClick ={
                        navController.navigateUp()
                    },

                    ) {
                    Icon(Icons.Rounded.MusicNote , contentDescription = "Localized description")
                }
                Spacer(modifier = Modifier.size(32.dp))
                SongCard(song = viewModel.song.value!!, navController = navController,
                    viewModel = viewModel)
            }
        },
        bottomBar = {BottomNavBar(navController = navController)})
}


@Preview(showBackground = true, device = Devices.PIXEL_3A)
@Composable
fun PreviewGDT() {
    AppTheme {
        ImageGeneratorDevTool(
            navController = rememberNavController(),
            viewModel = getViewModel()
        )
    }
}


