package at.ac.tuwien.inso.ui


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import at.ac.tuwien.inso.ui.navigation.SoundViewNavigation
import at.ac.tuwien.inso.ui.theme.AndroidArchitectureExampleTheme
import at.ac.tuwien.inso.ui.viewmodel.GenerateCoverViewModel
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import org.koin.androidx.viewmodel.ext.android.getViewModel



/**
 * This is the central point for the UI of the Application.
 * Only the Application [at.ac.tuwien.inso.App] is "above".
 *
 * This class handles the theming of the Application,
 * and defines the entry point for the UI.
 */
/*
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    private val REQUEST_CODE_STORAGE_PERMISSIONS = 1

    private fun requestStoragePermissions() {
        // Request the storage permissions
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            REQUEST_CODE_STORAGE_PERMISSIONS
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Check if the storage permissions have been granted
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                // The storage permissions have not been granted, show an error message
                // Toast.makeText(this, "Unable to access storage. The app will not function properly.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestStoragePermissions()
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_main) as NavHostFragment
        navController = navHostFragment.navController
        setSupportActionBar(findViewById(R.id.toolbar))
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}
*/

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // Inject the ViewModel with koin
            //val viewModel = getViewModel<GenerateCoverViewModel>()
            //val viewModel = getViewModel<GenerateCoverViewModel>()


            // This sets the correct theming for the Application
            AndroidArchitectureExampleTheme {

                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    navController = rememberNavController()
                    // The navigation host will handle the currently shown screen
                    SoundViewNavigation(navHostController = navController)
                }
            }
        }
    }
}
