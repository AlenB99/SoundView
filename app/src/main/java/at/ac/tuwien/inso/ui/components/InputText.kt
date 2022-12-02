package at.ac.tuwien.inso.ui.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction

/**
 * Easy to use function for adding a simple input field to a view.
 *
 * @param modifier [Modifier] to be applied to the button.
 * @param text The initial / current input value of the field.
 * @param label The label of the input field.
 * @param maxLine The maximum height in rows.
 * @param required Flag if the text field has to be filled. If true and empty, the field goes into error mode.
 * @param onImeAction Callback function executed when the "done" button in the keyboard is pressed.
 * @param onTextChange Callback function executed when the user changes the input text.
 */
@ExperimentalComposeUiApi
@Composable
fun InputText(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    maxLine: Int = 1,
    required: Boolean = false,
    onImeAction: () -> Unit = {},
    onTextChange: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent
        ),
        maxLines = maxLine,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            onImeAction()
            keyboardController?.hide()
        }),
        modifier = modifier,
        isError = required && text.isBlank()
    )
}
