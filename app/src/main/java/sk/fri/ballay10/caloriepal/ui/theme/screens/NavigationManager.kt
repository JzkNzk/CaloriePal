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
import sk.fri.ballay10.caloriepal.ui.theme.screens.ingredient.IngredientScreen
import sk.fri.ballay10.caloriepal.ui.theme.screens.mealsAndRecipes.RecipeAddingScreen
import sk.fri.ballay10.caloriepal.ui.theme.screens.mealsAndRecipes.RecipeScreen
import sk.fri.ballay10.caloriepal.ui.theme.screens.summary.CalorieScreen
import sk.fri.ballay10.caloriepal.viewModels.MealsAndRecipesViewModel
import sk.fri.ballay10.caloriepal.viewModels.SummaryPageViewModel

@Composable
fun NavigationManager(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DestinationsCaloriePal.CalorieScreen.name,
            modifier = Modifier.padding(innerPadding)
            ) {
            composable(DestinationsCaloriePal.CalorieScreen.name) { CalorieScreen() }
            composable(DestinationsCaloriePal.RecipeScreen.name) {
                RecipeScreen(
                    moveToAddingScreen = {screenType ->
                        if(screenType == 0) {
                            navController.navigate(AddingScreens.AddMeal.name)
                        } else {
                            navController.navigate(AddingScreens.AddRecipe.name)
                        }
                    }
                )
            }
            composable(DestinationsCaloriePal.IngredientScreen.name) { IngredientScreen() }
            composable(AddingScreens.AddMeal.name) { sk.fri.ballay10.caloriepal.ui.theme.screens.mealsAndRecipes.MealAddingScreen() }
            composable(AddingScreens.AddRecipe.name) { RecipeAddingScreen() }

        }
    }
}
