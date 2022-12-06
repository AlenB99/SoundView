package at.ac.tuwien.inso.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import at.ac.tuwien.inso.R
import at.ac.tuwien.inso.ui.theme.AndroidArchitectureExampleTheme
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * This is the central point for the UI of the Application.
 * Only the Application [at.ac.tuwien.inso.App] is "above".
 *
 * This class handles the theming of the Application,
 * and defines the entry point for the UI.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController;

    private val REQUEST_CODE_STORAGE_PERMISSIONS = 1

    private fun requestStoragePermissions() {
        // Request the storage permissions
        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ), REQUEST_CODE_STORAGE_PERMISSIONS)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Check if the storage permissions have been granted
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val navHostFragment =  supportFragmentManager
                    .findFragmentById(R.id.fragment_main) as NavHostFragment
                navController = navHostFragment.navController            } else {
                // The storage permissions have not been granted, show an error message
                Toast.makeText(this, "Unable to access storage. The app will not function properly.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestStoragePermissions()
        setContentView(R.layout.activity_main)

        //TODO Setup Bottom Navigation
        //val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bot)


    }
}
