package at.ac.tuwien.inso.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import at.ac.tuwien.inso.ui.screens.*
import at.ac.tuwien.inso.ui.viewmodel.SongViewModel


@RequiresApi(Build.VERSION_CODES.S)
@ExperimentalComposeUiApi
@Composable
fun SoundViewNavigation(navHostController: NavHostController, viewModel: SongViewModel) {
    // This is a "wrapper" view for showing the correct screen.
    // Initially, the "FriendList" is shown.
    val songs by viewModel.songs.collectAsState()

    NavHost(
        navController = navHostController,
        startDestination = SoundViewScreens.SoundRecorderScreen.route
    ) {
        composable(SoundViewScreens.ImageChooserScreen.route) {
        ImageChooser(
            navController = navHostController,
            viewModel = viewModel,
            prompt = viewModel.prompt.value.toString()
        )
        }

        composable(SoundViewScreens.ImageToStorageScreen.route) {
            ImageToStorage(navController = navHostController, viewModel = viewModel)
        }

        composable(SoundViewScreens.ImageGenerateDevToolScreen.route) {
            ImageGeneratorDevTool(navController = navHostController, viewModel = viewModel)
        }
        composable(SoundViewScreens.SoundRecorderScreen.route) {
            SoundRecorder(navController = navHostController, viewModel = viewModel)
        }

        composable(SoundViewScreens.SongHistoryScreen.route) {
            SongHistory(navController = navHostController, viewModel = viewModel, songs = songs)
        }
        composable(SoundViewScreens.ImageHistoryScreen.route) {
            ImageHistory(navController = navHostController, viewModel = viewModel, songs = songs)
        }

        composable(SoundViewScreens.LyricsKeywordsScreen.route) {
            LyricsKeywords(
                navController = navHostController,
                viewModel = viewModel
            )
        }
    }
}