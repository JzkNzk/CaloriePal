package sk.fri.ballay10.caloriepal.ui.theme.screens

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import sk.fri.ballay10.caloriepal.CaloriePalApplication
import sk.fri.ballay10.caloriepal.viewModels.MealAddingViewModel
import sk.fri.ballay10.caloriepal.viewModels.MealsAndRecipesViewModel
import sk.fri.ballay10.caloriepal.viewModels.RecipeAddingViewModel
import sk.fri.ballay10.caloriepal.viewModels.SummaryPageViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for MealsAndRecipesViewModel
        initializer {
            MealsAndRecipesViewModel(
                caloriePalApplication().container.recipeRepository,
                caloriePalApplication().container.mealRepository
            )
        }

//        initializer {
//            SummaryPageViewModel(
//                caloriePalApplication().container.summaryRepository,
//            )
//        }
        initializer {
            RecipeAddingViewModel(
                caloriePalApplication().container.recipeRepository,
            )
        }
        initializer {
            MealAddingViewModel(
                caloriePalApplication().container.mealRepository,
                caloriePalApplication().container.recipeRepository
            )
        }
    }
}

fun CreationExtras.caloriePalApplication(): CaloriePalApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CaloriePalApplication)