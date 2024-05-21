package sk.fri.ballay10.caloriepal.ui.theme

//AndroidX

import androidx.compose.material.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
//Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
//Local
import sk.fri.ballay10.caloriepal.ui.theme.screens.DestinationsCaloriePal
import sk.fri.ballay10.caloriepal.R

@Composable
fun TopDescriptionBar(title: String) {
    TopAppBar(
        backgroundColor = Color(50,50,50),
        title = {
            Text(title, color = Color.White, fontSize = 25.sp)
        }
    )
}

@Composable
fun BottomNavBar(navController: NavController) {
    val items = DestinationsCaloriePal.entries.toTypedArray()
    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { screen ->

            NavigationBarItem(
                icon = {
                    if (screen.title == "Summary") {
                        Icon( painter = painterResource(id = R.drawable.baseline_favorite_24), contentDescription = screen.title )
                     }
                    if (screen.title == "Meals & Recipes") {
                        Icon( painter = painterResource(id = R.drawable.baseline_restaurant_24), contentDescription = screen.title )
                    }
                    if (screen.title == "Ingredients") {
                        Icon( painter = painterResource(id = R.drawable.baseline_format_list_bulleted_24), contentDescription = screen.title )
                    }},
                label = { Text(screen.title) },
                selected = currentRoute == screen.name,
                onClick = {
                    navController.navigate(screen.name) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}




