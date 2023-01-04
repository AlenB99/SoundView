package at.ac.tuwien.inso.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.ac.tuwien.inso.ui.navigation.SoundViewScreens

@Composable
fun ImageGeneratorDevTool(navController: NavController) {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var text by remember { mutableStateOf(TextFieldValue("")) }
            TextField(
                value = text,
                onValueChange = { newText ->
                    text = newText
                }
            )
            Button(
                onClick = { navController.navigate(route = SoundViewScreens.ImageChooserScreen.route)},
                // Uses ButtonDefaults.ContentPadding by default
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 12.dp,
                    end = 20.dp,
                    bottom = 12.dp
                )
            ) {
                // Inner content including an icon and a text label
                Text("Generate")
            }

        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL_3A)
@Composable
fun PreviewGDT() {
    MaterialTheme {
        ImageGeneratorDevTool(
            navController = rememberNavController()
        )
    }
}


