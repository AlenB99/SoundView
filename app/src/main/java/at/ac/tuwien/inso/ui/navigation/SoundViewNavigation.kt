package at.ac.tuwien.inso.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import at.ac.tuwien.inso.ui.screens.ImageChooser
import at.ac.tuwien.inso.ui.screens.ImageToStorage


@ExperimentalComposeUiApi
@Composable
fun SoundViewNavigation(navHostController: NavHostController) {
    // This is a "wrapper" view for showing the correct screen.
    // Initially, the "FriendList" is shown.
    NavHost(
        navController = navHostController,
        startDestination = SoundViewScreens.ImageChooserScreen.route
    ) {
        composable(SoundViewScreens.ImageChooserScreen.route) {
            ImageChooser(navController = navHostController)
        }

        composable(SoundViewScreens.ImageToStorageScreen.route) {
            ImageToStorage(navController = navHostController)
        }
    }
}