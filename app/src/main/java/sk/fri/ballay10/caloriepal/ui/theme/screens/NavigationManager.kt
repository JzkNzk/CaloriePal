package sk.fri.ballay10.caloriepal.ui.theme.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import sk.fri.ballay10.caloriepal.objects.CalendarProvider
import sk.fri.ballay10.caloriepal.ui.theme.BottomNavBar
import sk.fri.ballay10.caloriepal.viewModels.MealsAndRecipesViewModel
import sk.fri.ballay10.caloriepal.viewModels.SummaryPageViewModel

@Composable
fun NavigationManager(mealsAndRecipesViewModel: MealsAndRecipesViewModel, summaryPageViewModel: SummaryPageViewModel) {
    val navController = rememberNavController()
    var selectedDateId by remember {
        mutableIntStateOf(CalendarProvider.todayId)
    }
    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DestinationsCaloriePal.CalorieScreen.name,
            modifier = Modifier.padding(innerPadding)
            ) {
            composable(route = DestinationsCaloriePal.CalorieScreen.name) {

            }
            composable(DestinationsCaloriePal.CalorieScreen.name) { CalorieScreen(summaryPageViewModel, onDateSelect = {selectedDate ->
                selectedDateId = selectedDate
            }) }
            composable(DestinationsCaloriePal.RecipeScreen.name) { RecipeScreen(mealsAndRecipesViewModel, addMealToSummary = {
                // Process summary when meal has been completed
                summaryPageViewModel.processSummary(meal = it, id = selectedDateId)
            }) }
            composable(DestinationsCaloriePal.IngredientScreen.name) { IngredientScreen() }
        }
    }
}
