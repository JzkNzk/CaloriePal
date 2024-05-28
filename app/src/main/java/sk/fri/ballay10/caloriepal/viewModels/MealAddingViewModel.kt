package sk.fri.ballay10.caloriepal.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import sk.fri.ballay10.caloriepal.data.Meal
import sk.fri.ballay10.caloriepal.data.Recipe
import sk.fri.ballay10.caloriepal.roomdb.meal.MealRepository
import sk.fri.ballay10.caloriepal.roomdb.recipe.RecipeRepository

class MealAddingViewModel(
    private val mealRepository: MealRepository,
    recipeRepository: RecipeRepository
) : ViewModel() {

    val recipeList: StateFlow<List<Recipe>> =
        recipeRepository.getAllRecipesStream().map { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = listOf()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    var mealUiState by mutableStateOf(MealUiState())
        private set

    var choosingRecipeUiState by mutableStateOf(ChoosenRecipeUiState())
        private set

    fun updateMealUiState(mealDetails: MealDetails) {
        mealUiState =
            MealUiState(createdMeal = mealDetails, isEntryValid = validateMeal())
    }

    private fun validateMeal(): Boolean {
        return mealUiState.createdMeal.name.isNotBlank() && mealUiState.createdMeal.recipes.isNotEmpty()
    }

    fun updateChoosingUiState(chosenRecipeDetails: ChoosenRecipeDetails) {
        choosingRecipeUiState =
            ChoosenRecipeUiState(
                chosenRecipeDetails = chosenRecipeDetails,
                isEntryValid = validateChosenRecipe()
            )
    }

    fun addChosenRecipeToMeal() {
        if (validateChosenRecipe()) {
            val convertedRecipe = choosingRecipeUiState.chosenRecipeDetails.toRecipe()
            val updatedListOfRecipes = mealUiState.createdMeal.recipes.toMutableList()

            if (convertedRecipe != null) {
                updatedListOfRecipes.add(convertedRecipe)
            }
            val updatedCalories = (convertedRecipe?.totalCalories ?: 0) + mealUiState.createdMeal.totalCalories.toInt()
            val updatedProtein = (convertedRecipe?.totalProtein ?: 0) + mealUiState.createdMeal.totalProtein.toInt()
            val updatedFats = (convertedRecipe?.totalFats ?: 0) + mealUiState.createdMeal.totalFats.toInt()
            val updatedCarbs = (convertedRecipe?.totalCarbs ?: 0) + mealUiState.createdMeal.totalCarbs.toInt()
            val updatedMeal = mealUiState.createdMeal.copy(
                recipes = updatedListOfRecipes,
                recipeCount = updatedListOfRecipes.size.toString(),
                totalCalories = updatedCalories.toString(),
                totalProtein = updatedProtein.toString(),
                totalCarbs = updatedCarbs.toString(),
                totalFats = updatedFats.toString()
            )
            updateMealUiState(updatedMeal)
            updateChoosingUiState(ChoosenRecipeDetails())
        }

    }

    fun removeChosenRecepieFromMeal(recipe: Recipe) {
        val updatedListOfRecipes = mealUiState.createdMeal.recipes.toMutableList()
        updatedListOfRecipes.remove(recipe)

        val updatedCalories = mealUiState.createdMeal.totalCalories.toInt() - recipe.totalCalories
        val updatedProtein = mealUiState.createdMeal.totalProtein.toInt() - recipe.totalProtein
        val updatedFats = mealUiState.createdMeal.totalFats.toInt() - recipe.totalFats
        val updatedCarbs = mealUiState.createdMeal.totalCarbs.toInt() - recipe.totalCarbs

        val updatedMeal = mealUiState.createdMeal.copy(
            recipes = updatedListOfRecipes,
            recipeCount = updatedListOfRecipes.size.toString(),
            totalCalories = updatedCalories.toString(),
            totalProtein = updatedProtein.toString(),
            totalCarbs = updatedCarbs.toString(),
            totalFats = updatedFats.toString()
        )
        updateMealUiState(updatedMeal)
    }

    private fun validateChosenRecipe(): Boolean {
        return choosingRecipeUiState.chosenRecipeDetails.recipe != null
    }

    suspend fun saveMeal() {
        if (validateMeal()) {
            mealRepository.insertMeal(mealUiState.createdMeal.toMeal())
        }
    }
}


data class MealUiState(
    val createdMeal: MealDetails = MealDetails(),
    val isEntryValid: Boolean = false
)

data class ChoosenRecipeUiState(
    val chosenRecipeDetails: ChoosenRecipeDetails = ChoosenRecipeDetails(),
    val isEntryValid: Boolean = false
)

data class ChoosenRecipeDetails(
    val recipe: Recipe? = null,
)

data class MealDetails(
    val id: Int = 0,
    val name: String = "",
    val recipes: MutableList<Recipe> = mutableListOf(),
    val recipeCount: String = "0",
    val totalCalories: String = "0",
    val totalProtein: String = "0",
    val totalFats: String = "0",
    val totalCarbs: String = "0"
)

fun ChoosenRecipeDetails.toRecipe() : Recipe? = recipe?.let {
    Recipe(
        name = it.name,
        ingredients = it.ingredients,
        ingredientCount = it.ingredientCount,
        totalCalories = it.totalCalories,
        totalProtein = it.totalProtein,
        totalCarbs = it.totalCarbs,
        totalFats = it.totalFats
    )
}

fun MealDetails.toMeal(): Meal = Meal(
    id = id,
    name = name,
    recipes = recipes.toList(),
    recipeCount = recipeCount.toIntOrNull() ?: 0,
    totalCalories = totalCalories.toIntOrNull() ?: 0,
    totalProtein = totalProtein.toIntOrNull() ?: 0,
    totalFats = totalFats.toIntOrNull() ?: 0,
    totalCarbs = totalCarbs.toIntOrNull() ?: 0
)

fun Meal.toMealDetails(): MealDetails = MealDetails(
    id = id,
    name = name,
    recipes = recipes.toMutableList(),
    recipeCount = recipeCount.toString(),
    totalCalories = totalCalories.toString(),
    totalProtein = totalProtein.toString(),
    totalFats = totalFats.toString(),
    totalCarbs = totalCarbs.toString()
)
