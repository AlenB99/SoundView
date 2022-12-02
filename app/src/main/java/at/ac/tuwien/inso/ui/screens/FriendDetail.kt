package at.ac.tuwien.inso.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.ac.tuwien.inso.R
import at.ac.tuwien.inso.model.Friend
import at.ac.tuwien.inso.ui.components.Button
import at.ac.tuwien.inso.ui.components.InputText
import at.ac.tuwien.inso.ui.theme.ColorError
import kotlin.random.Random

/**
 * This screen shows the details for the friend if given.
 * Otherwise this creates a clean form for adding a new friend.
 *
 * The save and navigation functions are given by the caller (ie. FriendNavigation).
 *
 * @param friend The [Friend] to update. If null, an empty form is shown.
 * @param onSaveFriend Callback function to save the given [Friend].
 * @param navigateUp Callback function to navigate back to the previous screen.
 */
@Composable
@ExperimentalComposeUiApi
fun FriendDetail(friend: Friend?, onSaveFriend: (Friend) -> Unit, navigateUp: () -> Unit) {
    val context = LocalContext.current

    // rememberSaveable will keep the values when rotating the device
    var name by rememberSaveable { mutableStateOf(friend?.name ?: "") }
    var score by rememberSaveable { mutableStateOf(friend?.score?.toFloat() ?: 1f) }

    // Use the incoming "friend" parameter to determine the correct title depending on the current use case
    val titleResourceId = if (friend == null) {
        R.string.friend_detail_title_new
    } else {
        R.string.friend_detail_title_edit
    }

    // This is the parent layout for the composable content
    Scaffold(
        // Show a top bar with a back button and a custom title depending on the use case
        topBar = { FriendDetailTopBar(titleResourceId, navigateUp) }
    ) {

        // Wrap content in a column with a central padding for the full layout
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            // Show an input text field to enter the name of the "friend"
            // The current value - initially the name of the given friend if available, or empty
            // is set as a value.
            // A label is shown as a hint for the user.
            // "required" is a custom field inside the "InputText" component, which
            // changes the color of the borders to "error" if empty.
            // The ime action allows to save the friend if the user presses the "done" button
            // on the keyboard.
            // The callback for "onTextChange" ensures that the entered value is persisted
            // to our "remember" variable.
            InputText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                text = name,
                label = stringResource(R.string.friend_name),
                required = true,
                onImeAction = {
                    if (name.isNotBlank()) {
                        saveInput(friend, name, score, onSaveFriend, context)
                    }
                },
                onTextChange = { name = it }
            )

            // Shows an error message if text field is empty
            if (name.isBlank()) {
                Text(stringResource(id = R.string.friend_error_name_empty), color = ColorError)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // A simple caption for the slider
            Text(text = stringResource(R.string.friend_score_caption), fontWeight = FontWeight.Bold)

            // The slider used to enter the "best friend score".
            // Like in the text field, the "onValueChange" callback ensures that
            // the entered value is stored to our "remember" variable.
            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                value = score,
                valueRange = 0f..10f,
                steps = 10,
                onValueChange = { score = it }
            )

            // This shows the current value of the slider in a text label
            Text(
                text = score.toInt().toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // A button to save the current input.
            // The view model handles the differences between adding and updating a friend.
            // It is only enabled, if the name is not empty.
            Button(
                text = stringResource(R.string.save),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                enabled = name.isNotBlank(),
                onClick = {
                    saveInput(friend, name, score, onSaveFriend, context)
                }
            )
        }
    }
}

/**
 * Simple top bar showing the given string as a title.
 * Also, a back button is added which triggers the [navigateUp] function.
 *
 * @param titleResourceId The resource ID of the expected title String.
 * @param navigateUp Callback function to navigate back to the previous screen.
 */
@Composable
private fun FriendDetailTopBar(titleResourceId: Int, navigateUp: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(id = titleResourceId)) },
        backgroundColor = MaterialTheme.colors.primary,
        navigationIcon = {
            IconButton(onClick = { navigateUp() }) {
                Icon(Icons.Filled.ArrowBack, stringResource(id = R.string.navigate_back))
            }
        })
}

/**
 * Helper function to handle the save operation.
 *
 * @param friend The optional [Friend] if in edit mode.
 * @param name The newly entered name of the Friend.
 * @param score The "best friend score"
 * @param onSaveFriend The callback function to save a [Friend].
 * @param context The UI [Context] used to load String's and show a toast.
 */
private fun saveInput(
    friend: Friend?,
    name: String,
    score: Float,
    onSaveFriend: (Friend) -> Unit,
    context: Context
) {
    val toastMessage =
        if (friend != null) {
            // Update Friend
            friend.name = name
            friend.score = score.toInt()
            onSaveFriend(friend)
            context.getString(R.string.friend_updated)
        } else {
            // Add Friend with generated random ID (should usually be done on the backend side)
            val newFriend = Friend(
                id = Random.nextInt(),
                name = name,
                score = score.toInt()
            )
            onSaveFriend(newFriend)
            context.getString(R.string.friend_added)
        }

    // Show a success toast with a message depending on the use case
    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
}

@Composable
@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Preview(showBackground = true, locale = "de")
fun FriendDetailPreview() {
    FriendDetail(friend = null, onSaveFriend = {}, navigateUp = {})
}

@Composable
@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Preview(showBackground = true, locale = "de")
fun FriendDetailPreview_edit() {
    FriendDetail(friend = Friend(
        id = 123,
        name = "Buddy",
        score = 7
    ), onSaveFriend = {}, navigateUp = {})
}
