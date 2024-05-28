package sk.fri.ballay10.caloriepal.ui.theme.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import sk.fri.ballay10.caloriepal.objects.CalendarProvider
import sk.fri.ballay10.caloriepal.ui.theme.BottomNavBar
import sk.fri.ballay10.caloriepal.ui.theme.screens.ingredient.IngredientScreen
import sk.fri.ballay10.caloriepal.ui.theme.screens.mealsAndRecipes.MealAddingScreen
import sk.fri.ballay10.caloriepal.ui.theme.screens.mealsAndRecipes.RecipeAddingScreen
import sk.fri.ballay10.caloriepal.ui.theme.screens.mealsAndRecipes.RecipeScreen
import sk.fri.ballay10.caloriepal.ui.theme.screens.summary.CalorieScreen
import sk.fri.ballay10.caloriepal.viewModels.MealsAndRecipesViewModel
import sk.fri.ballay10.caloriepal.viewModels.SummaryPageViewModel

@Composable
fun NavigationManager(navController: NavHostController = rememberNavController(), summaryViewModel: SummaryPageViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DestinationsCaloriePal.CalorieScreen.name,
            modifier = Modifier.padding(innerPadding)
            ) {
            composable(DestinationsCaloriePal.CalorieScreen.name) { CalorieScreen(summaryViewModel) }
            composable(DestinationsCaloriePal.RecipeScreen.name) {
                RecipeScreen(
                    moveToAddingScreen = {screenType ->
                        if(screenType == 0) {
                            navController.navigate(AddingScreens.AddMeal.name)
                        } else {
                            navController.navigate(AddingScreens.AddRecipe.name)
                        }
                    },
                    summaryDetails = summaryViewModel.summaryUiState.displayedSummary,
                    onMealToSummary = {summaryDetails ->
                        coroutineScope.launch { summaryViewModel.updateSummaryUiStateStatistics(summaryDetails) }
                    }
                )
            }
            composable(DestinationsCaloriePal.IngredientScreen.name) { IngredientScreen() }
            composable(AddingScreens.AddMeal.name) { MealAddingScreen(returnBack = {
                navController.popBackStack()
            }) }
            composable(AddingScreens.AddRecipe.name) { RecipeAddingScreen(returnBack = {
                navController.popBackStack()
            }) }

        }
    }
}
