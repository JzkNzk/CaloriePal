package sk.fri.ballay10.caloriepal.ui.theme

//AndroidX

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
//Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigamole.composeshadowsplus.common.shadowsPlus
//Local
import sk.fri.ballay10.caloriepal.ui.theme.screens.DestinationsCaloriePal
import sk.fri.ballay10.caloriepal.R

@Composable
fun TopDescriptionBar(title: String, bgColor: Color = Color(50,50,50)) {
    TopAppBar(
        backgroundColor = bgColor,
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
        items.forEach { destinations ->

            NavigationBarItem(
                icon = {
                    if (destinations.title == "Summary") {
                        Icon( painter = painterResource(id = R.drawable.baseline_favorite_24), contentDescription = destinations.title )
                     }
                    if (destinations.title == "Meals & Recipes") {
                        Icon( painter = painterResource(id = R.drawable.baseline_restaurant_24), contentDescription = destinations.title )
                    }
                    if (destinations.title == "Ingredients") {
                        Icon( painter = painterResource(id = R.drawable.baseline_format_list_bulleted_24), contentDescription = destinations.title )
                    }},
                label = { Text(destinations.title) },
                selected = currentRoute == destinations.name,
                onClick = {
                    navController.navigate(destinations.name)
                    {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
fun EnterNameForm(modifier: Modifier = Modifier, nameOfWhat: String, onNameChange: (String) -> Unit, value: String = "") {
    Column (
        modifier = modifier
            .shadowsPlus()
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(15.dp))
            .padding(8.dp)
    ) {
        Text(text = "Enter $nameOfWhat name: ", fontSize = 18.sp)
        OutlinedTextField(
            value = value,
            onValueChange = {
                onNameChange(it)
            },
            label = { Text(text = nameOfWhat.replaceFirstChar { it.titlecase() })},
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ActionButtons(onConfirm: () -> Unit, onCancel: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
    ) {
        androidx.compose.material3.Button(onClick = { onCancel() }, contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp, start = 40.dp, end = 40.dp), colors = ButtonDefaults.buttonColors(containerColor = colorRed1)) {
            Icon(Icons.Filled.Close, contentDescription = "Cancel")
        }
        androidx.compose.material3.Button(onClick = { onConfirm() }, contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp, start = 40.dp, end = 40.dp), colors = ButtonDefaults.buttonColors(containerColor = colorGreen1)) {
            Icon(Icons.Filled.Done, contentDescription = "Done" )
        }
    }
}




