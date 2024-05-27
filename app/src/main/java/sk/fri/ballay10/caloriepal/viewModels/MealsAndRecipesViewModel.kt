package sk.fri.ballay10.caloriepal.viewModels

import androidx.lifecycle.ViewModel
import sk.fri.ballay10.caloriepal.data.Meal
import sk.fri.ballay10.caloriepal.data.Recipe
import sk.fri.ballay10.caloriepal.roomdb.meal.MealRepository
import sk.fri.ballay10.caloriepal.roomdb.recipe.RecipeRepository

class MealsAndRecipesViewModel(private val recipeRepository: RecipeRepository, private val mealRepository: MealRepository) : ViewModel() {


    suspend fun saveRecipe() {
        //recipeRepository.insertRecipe()
    }

    suspend fun saveMeal() {
        //mealRepository.insertMeal()
    }
}