package at.ac.tuwien.inso.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.tuwien.inso.R
import at.ac.tuwien.inso.ui.navigation.SoundViewScreens
import coil.compose.rememberAsyncImagePainter
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform


@Composable
fun ImageChooser(navController: NavController, prompt: String) {
    val urlList = pythonScript(prompt);


    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val imageModifier = Modifier
                .size(150.dp)
                .padding(5.dp)
                .clip(RoundedCornerShape(25.dp))
                .clickable(onClick = {
                    navController.navigate(route = SoundViewScreens.ImageToStorageScreen.route)
                })
            Row{
                Image(
                    painter = rememberAsyncImagePainter(urlList[0]),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = imageModifier
                )

                Image(
                    painter = painterResource(id = R.drawable.placeholder),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = imageModifier
                )
            }

            Row{
                Image(
                    painter = painterResource(id = R.drawable.placeholder),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = imageModifier
                )

                Image(
                    painter = painterResource(id = R.drawable.placeholder),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = imageModifier
                )
            }
            Text(text = "Choose one to Download!", style = MaterialTheme.typography.caption)
            SongTitle(name = "world")
            Artist(name = "world")
            Keywords(name = "world")
        }
    }
}

@Composable
fun pythonScript(prompt: String): List<String> {
    var urlList: List<String> = listOf<String>("", "", "", "")
    if (!Python.isStarted()) {
        Python.start(AndroidPlatform(LocalContext.current))
    }

    val py = Python.getInstance()
    val module = py.getModule("image_generate")
    try {
        val url = module.callAttr("image_generate", prompt)
            .toString()
         urlList = url.split(",").map {
            it.trim()
                .replace("[", "").replace("]", "")
                .replace("'", "")
        }
        return urlList;
        // From python script we get a PyObject, which is converted to a string. Afterwards
        // its added to urlList, so that we can select the urls through indexing
    } catch (e: PyException) {
        println(e.message + " ")
    }
    return urlList;
}

@Composable
    fun SongTitle(name: String) {
        Text(text = "Hello, $name!", style = MaterialTheme.typography.h3)
    }
    @Composable
    fun Artist(name: String) {
        Text(text = "Hello, $name!", style = MaterialTheme.typography.body1, textAlign = TextAlign.Start)
    }
    @Composable
    fun Keywords(name: String) {
        Text(text = "Hello, $name!", style = MaterialTheme.typography.caption)
    }

    @Preview(showBackground = true, device = Devices.PIXEL_3A)
    @Composable
    fun PreviewGreeting() {
        MaterialTheme {
            ImageChooser(
                navController = rememberNavController(),
                prompt = "test"
            )
        }
    }
