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


@Composable
fun ImageChooser(navController: NavController) {
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
                navController = rememberNavController()
            )
        }
    }
