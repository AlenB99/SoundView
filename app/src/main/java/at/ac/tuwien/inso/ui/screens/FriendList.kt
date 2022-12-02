package at.ac.tuwien.inso.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.ac.tuwien.inso.R
import at.ac.tuwien.inso.model.Friend

/**
 * This screen shows a list of the given [Friend]s.
 * If the list is empty, a hint is shown.
 *
 * @param friends The list of given [Friend]s.
 * @param onViewFriend Callback function triggered when the user clicks on an entry in the list.
 * @param onDeleteFriend Callback function triggered when the user wants to delete a [Friend].
 * @param onAddFriend Callback function when the user wants to add a new [Friend].
 * @param onRefresh Callback function which should refresh the list of [Friend]s from the API.
 */
@Composable
@ExperimentalComposeUiApi
fun FriendList(
    friends: List<Friend>,
    onViewFriend: (Friend) -> Unit,
    onDeleteFriend: (Friend) -> Unit,
    onAddFriend: () -> Unit,
    onRefresh: () -> Unit
) {
    // "Scaffold" provide an easy way to define extra components like a top bar or a floating action button
    // in addition to the content.
    Scaffold(
        topBar = { FriendListTopBar(onRefresh) },
        floatingActionButton = { FriendListFAB(onAddFriend) },
        modifier = Modifier.fillMaxSize()
    ) {
        // "Column" vertically aligns its child components, and is useful for a simple list.
        // "LazyColumn" only renders the currently visible content, and therefore improves
        // the performance for large lists.
        LazyColumn {
            items(friends) { friend ->
                FriendRow(
                    friend = friend,
                    onViewFriend = { onViewFriend(it) },
                    onDeleteFriend = { onDeleteFriend(it) }
                )
            }
        }

        // This shows a hint if the given list of friends is empty.
        if (friends.isEmpty()) {
            // The "Box" is needed to center a text in the full screen (could also be a "Column").
            // Without the wrapper, the content can only be aligned horizontally, not vertically.
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.msg_no_friends),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * The top bar implementation for the [FriendList].
 * It shows a title and a refresh button, which triggers [onRefresh].
 *
 * @param onRefresh Callback function which should refresh the list of [Friend]s from the API.
 */
@Composable
private fun FriendListTopBar(onRefresh: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        actions = {
            IconButton(onClick = onRefresh) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = stringResource(R.string.refresh),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
        backgroundColor = MaterialTheme.colors.primary
    )
}

/**
 * The floating action button, which triggers [onAddFriend] when clicked.
 * It contains a "+" icon.
 *
 * @param onAddFriend Callback function executed when the user clicks the FAB.
 */
@Composable
private fun FriendListFAB(onAddFriend: () -> Unit) {
    FloatingActionButton(
        modifier = Modifier
            .padding(8.dp),
        onClick = onAddFriend,
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
    ) {
        Icon(Icons.Filled.Add, stringResource(R.string.add_friend))
    }
}

/**
 * Layout for a single row for the list of [Friend]s.
 *
 * @param friend The given [Friend] for the current row.
 * @param onViewFriend Callback function executed when the row is clicked.
 * @param onDeleteFriend Callback function executed when the delete button is clicked.
 */
@Composable
private fun FriendRow(
    friend: Friend,
    onViewFriend: (Friend) -> Unit,
    onDeleteFriend: (Friend) -> Unit
) {
    Surface(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(2.dp)),
        border = BorderStroke(1.dp, color = Color.LightGray),
        elevation = 4.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                Modifier
                    .width(0.dp)
                    .weight(1f)
                    .clickable { onViewFriend(friend) }
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = friend.name,
                    style = MaterialTheme.typography.subtitle1, fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.friend_score, friend.score),
                    style = MaterialTheme.typography.caption
                )
            }

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.delete),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable { onDeleteFriend(friend) }
            )
        }
    }
}

@Composable
@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Preview(showBackground = true, locale = "de")
fun FriendListPreview() {
    FriendList(
        friends = listOf(
            Friend(123, "Buddy", 3),
            Friend(123, "Paul", 7),
            Friend(123, "Gabriel", 10)
        ),
        onViewFriend = {},
        onDeleteFriend = {},
        onAddFriend = {},
        onRefresh = {}
    )
}

@Composable
@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Preview(showBackground = true, locale = "de")
fun FriendListPreview_empty() {
    FriendList(
        friends = emptyList(),
        onViewFriend = {},
        onDeleteFriend = {},
        onAddFriend = {},
        onRefresh = {}
    )
}

@Composable
@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Preview(showBackground = true, locale = "de")
fun FriendRowPreview() {
    FriendRow(friend = Friend(123, "Buddy", 7), onViewFriend = {}, onDeleteFriend = {})
}
