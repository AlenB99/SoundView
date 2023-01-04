package at.ac.tuwien.inso.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import at.ac.tuwien.inso.ui.screens.ImageChooser
import at.ac.tuwien.inso.ui.screens.ImageGeneratorDevTool
import at.ac.tuwien.inso.ui.screens.ImageToStorage
import at.ac.tuwien.inso.ui.viewmodel.GenerateCoverViewModel


@ExperimentalComposeUiApi
@Composable
fun SoundViewNavigation(navHostController: NavHostController) {
    // This is a "wrapper" view for showing the correct screen.
    // Initially, the "FriendList" is shown.


    NavHost(
        navController = navHostController,
        startDestination = SoundViewScreens.ImageGenerateDevToolScreen.route
    ) {
        composable(SoundViewScreens.ImageChooserScreen.route) {
        ImageChooser(
            navController = navHostController, prompt = "test"
        )
        }

        composable(SoundViewScreens.ImageToStorageScreen.route) {
            ImageToStorage(navController = navHostController)
        }

        composable(SoundViewScreens.ImageGenerateDevToolScreen.route) {
            ImageGeneratorDevTool(navController = navHostController)
        }
    }
}