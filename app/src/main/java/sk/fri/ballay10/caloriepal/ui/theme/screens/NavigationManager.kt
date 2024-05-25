package sk.fri.ballay10.caloriepal.ui.theme.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import sk.fri.ballay10.caloriepal.objects.CalendarProvider
import sk.fri.ballay10.caloriepal.ui.theme.BottomNavBar
import sk.fri.ballay10.caloriepal.viewModels.MealsAndRecipesViewModel
import sk.fri.ballay10.caloriepal.viewModels.SummaryPageViewModel

@Composable
fun NavigationManager(navController: NavHostController = rememberNavController(),mealsAndRecipesViewModel: MealsAndRecipesViewModel, summaryPageViewModel: SummaryPageViewModel) {
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
            composable(DestinationsCaloriePal.CalorieScreen.name) { CalorieScreen(summaryPageViewModel, onDateSelect = {selectedDate ->
                selectedDateId = selectedDate
            }) }
            composable(DestinationsCaloriePal.RecipeScreen.name) {
                RecipeScreen(mealsAndRecipesViewModel,
                    moveToAddingScreen = {screenType ->
                                     if(screenType == 0) {
                                         navController.navigate(AddingScreens.AddMeal.name)
                                     } else {
                                         navController.navigate(AddingScreens.AddRecipe.name)
                                     }
                    },
                    addMealToSummary = {
                    // Process summary when meal has been completed
                    summaryPageViewModel.processSummary(meal = it, id = selectedDateId)
            }) }
            composable(DestinationsCaloriePal.IngredientScreen.name) { IngredientScreen() }
            composable(AddingScreens.AddMeal.name) { MealAddingScreen()}
            composable(AddingScreens.AddRecipe.name) { RecipeAddingScreen()}

        }
    }
}
