package at.ac.tuwien.inso.ui


import android.widget.Toast
import android.media.Image
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import at.ac.tuwien.inso.ImageChooser
import at.ac.tuwien.inso.R
import java.io.File

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

        setContentView(R.layout.fragment_generate_cover)
        /*val clickme = findViewById<Button>(R.id.downloadButton)
        clickme.setOnClickListener{
            Toast.makeText(this,"Button Clicked", Toast.LENGTH_SHORT).show()

        }*/

    }
    /*public fun saveImageToStorage(filename: String, imageObject: Image ): File? {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(filename, ".jpg", storageDir)
        val photoPath = image.absolutePath
        return image
    }*/



}







