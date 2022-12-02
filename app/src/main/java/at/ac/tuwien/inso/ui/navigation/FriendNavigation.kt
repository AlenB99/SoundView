package at.ac.tuwien.inso.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import at.ac.tuwien.inso.model.Friend
import at.ac.tuwien.inso.ui.screens.FriendDetail
import at.ac.tuwien.inso.ui.screens.FriendList
import at.ac.tuwien.inso.ui.viewmodel.FriendViewModel

private const val FRIEND_DETAIL_ID_ARG = "friendId"

/**
 * This is a wrapper view for handling the navigation for all friend-related screens.
 * The created composable contains a [NavHost] including [FriendList] and [FriendDetails].
 * It also supplies navigation functions between the screens.
 *
 * @param viewModel The initialized view model for the friend-related screens.
 */
@ExperimentalComposeUiApi
@Composable
fun FriendNavigation(viewModel: FriendViewModel) {
    val navController = rememberNavController()
    val friends by viewModel.friends.collectAsState()

    // This is a "wrapper" view for showing the correct screen.
    // Initially, the "FriendList" is shown.
    NavHost(
        navController = navController,
        startDestination = FriendScreens.ListScreen.route
    ) {

        // This adds a route for the "FriendList" Screen.
        composable(FriendScreens.ListScreen.route) {
            // The navigation class supplies to list of friends, and callbacks for all the
            // functionality used inside the Screen (ie. refresh and delete).
            // It also provides callback functions to navigate between screens (ie. detail screen).
            FriendList(
                friends,
                onViewFriend = { navController.navigate(FriendScreens.DetailScreen.route + "?${FRIEND_DETAIL_ID_ARG}=${it.id}") },
                onAddFriend = { navController.navigate(FriendScreens.DetailScreen.route) },
                onDeleteFriend = { viewModel.deleteFriend(it) },
                onRefresh = { viewModel.refreshFriends() }
            )
        }

        // This adds a route for the "FriendDetail" Screen with a custom parameter "friendId"
        composable(
            FriendScreens.DetailScreen.route + "?${FRIEND_DETAIL_ID_ARG}={${FRIEND_DETAIL_ID_ARG}}",
            arguments = listOf(
                navArgument(name = FRIEND_DETAIL_ID_ARG) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            // If the route contains an ID, the friend will be loaded and added as a parameter
            // to the "FriendDetail" Screen. Otherwise, no friend is set.
            val friendId = backStackEntry.arguments?.getInt(FRIEND_DETAIL_ID_ARG)
            val friend: Friend? = friendId?.let {
                viewModel.getFriendById(friendId)
            }

            FriendDetail(
                friend,
                onSaveFriend = {
                    viewModel.insertFriend(it)
                    navController.navigateUp()
                },
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }
    }
}
