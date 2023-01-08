import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import at.ac.tuwien.inso.ui.theme.md_theme_light_onPrimaryContainer
import at.ac.tuwien.inso.ui.theme.md_theme_light_primaryContainer

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
)

val bottomNavItems = listOf(
    BottomNavItem(
        name = "Song Search",
        route = "home",
        icon = Icons.Rounded.Search,
    ),
    BottomNavItem(
        name = "Image History",
        route = "add",
        icon = Icons.Rounded.List,
    ),
    BottomNavItem(
        name = "Song History",
        route = "settings",
        icon = Icons.Rounded.List,
    ),
)


@Composable
fun BottomNavBar(navController : NavController){
    val backStackEntry = navController.currentBackStackEntryAsState()
    NavigationBar(
        containerColor = md_theme_light_primaryContainer,
    ) {
        bottomNavItems.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route

            NavigationBarItem(
                selected = selected,
                onClick = { navController.navigate(item.route) },
                label = {
                    Text(
                        text = item.name,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = "${item.name} Icon",
                    )
                }
            )
        }
    }
}