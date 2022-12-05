package at.ac.tuwien.inso.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =  supportFragmentManager
            .findFragmentById(R.id.fragment_main) as NavHostFragment
        navController = navHostFragment.navController
        //TODO Setup Bottom Navigation
        //val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bot)


    }
}
