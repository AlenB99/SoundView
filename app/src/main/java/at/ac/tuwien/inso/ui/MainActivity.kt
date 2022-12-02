package at.ac.tuwien.inso.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import at.ac.tuwien.inso.ui.navigation.FriendNavigation
import at.ac.tuwien.inso.ui.theme.AndroidArchitectureExampleTheme
import at.ac.tuwien.inso.ui.viewmodel.FriendViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * This is the central point for the UI of the Application.
 * Only the Application [at.ac.tuwien.inso.App] is "above".
 *
 * This class handles the theming of the Application,
 * and defines the entry point for the UI.
 */
@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // Inject the ViewModel with koin
            val viewModel = getViewModel<FriendViewModel>()

            // This sets the correct theming for the Application
            AndroidArchitectureExampleTheme {

                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {

                    // The navigation host will handle the currently shown screen
                    FriendNavigation(viewModel)
                }
            }
        }
    }
}
