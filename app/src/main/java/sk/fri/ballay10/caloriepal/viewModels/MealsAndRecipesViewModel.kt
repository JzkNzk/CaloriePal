package sk.fri.ballay10.caloriepal.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sk.fri.ballay10.caloriepal.MainActivity
import sk.fri.ballay10.caloriepal.data.Meal
import sk.fri.ballay10.caloriepal.data.Recipe

class MealsAndRecipesViewModel : ViewModel() {
    private val recipeDao = MainActivity.recipeDatabase.getRecipeDao()
    private val mealDao = MainActivity.mealDatabase.getMealDao()

    val recipeList : LiveData<List<Recipe>> = recipeDao.getAllRecipes()
    val mealList : LiveData<List<Meal>> = mealDao.getAllMeals()

    //Recipes functions

    fun addRecipeToList(recipe: Recipe) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeDao.addRecipe(recipe)
        }
    }

    fun removeRecipeFromList(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeDao.deleteRecipe(id)
        }
    }

    //Meals functions

    fun addMealToList(meal: Meal) {
        viewModelScope.launch(Dispatchers.IO) {
            mealDao.addMeal(meal)
        }
    }

    fun removeMealFromList(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mealDao.deleteMeal(id)
        }
    }
}