package at.ac.tuwien.inso.ui.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Easy to use function for adding simple buttons with a text to a view.
 *
 * @param modifier [Modifier] to be applied to the button.
 * @param text The content of the button.
 * @param enabled Flag, if the button should be enabled.
 * @param onClick Callback function executed on button click.
 */
@Composable
fun Button(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        enabled = enabled,
        modifier = modifier
    ) {
        Text(text)
    }
}
