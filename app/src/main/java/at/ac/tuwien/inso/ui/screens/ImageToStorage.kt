package at.ac.tuwien.inso.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.tuwien.inso.R
import at.ac.tuwien.inso.ui.navigation.SoundViewScreens
import at.ac.tuwien.inso.ui.viewmodel.GenerateCoverViewModel
import coil.compose.rememberAsyncImagePainter
import org.koin.androidx.compose.getViewModel

@Composable
fun ImageToStorage(navController: NavController, viewModel: GenerateCoverViewModel) {
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

            Image(
                painter =  rememberAsyncImagePainter(viewModel.imageurl.value),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = imageModifier
            )

            SongTitle(name = "world")
            Artist(name = "world")
            Keywords(name = "world")

            Button(
                onClick = {
                          //#TODO DOWNLOAD FUNCTION
                },
                // Uses ButtonDefaults.ContentPadding by default
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 12.dp,
                    end = 20.dp,
                    bottom = 12.dp
                )
            ) {
                // Inner content including an icon and a text label
                Text("Download")
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_3A)
@Composable
fun PreviewItS() {
    MaterialTheme {
        ImageToStorage(
            navController = rememberNavController(),
            viewModel = getViewModel()
        )
    }
}



