package sk.fri.ballay10.caloriepal.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import sk.fri.ballay10.caloriepal.data.ChoosenIngredient
import sk.fri.ballay10.caloriepal.data.Ingredient
import sk.fri.ballay10.caloriepal.data.Recipe
import sk.fri.ballay10.caloriepal.objects.IngredietList
import sk.fri.ballay10.caloriepal.roomdb.recipe.RecipeRepository

class RecipeAddingViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    val ingredientList = IngredietList.ingredients

    var recipeUiState by mutableStateOf(RecipeUiState())
        private set

    var choosingIngredientUiState by mutableStateOf(ChoosingIngredientUiState())
        private set

    fun updateRecipeUiState(recipeDetails: RecipeDetails) {
        recipeUiState = RecipeUiState(createdRecipe = recipeDetails, isEntryValid = validateRecipe())
    }

    private fun validateRecipe(): Boolean {
        return recipeUiState.createdRecipe.name.isNotBlank() && recipeUiState.createdRecipe.ingredients.isNotEmpty()
    }

    fun updateChoosingUiState(chosenIngredientDetails: ChoosenIngredientDetails) {
        choosingIngredientUiState =
            ChoosingIngredientUiState(chosenIngredientDetails = chosenIngredientDetails, isEntryValid = validateChosenIngredient())
    }

    fun addChosenIngredientToRecipe() {
        if (validateChosenIngredient()) {
            val convertedIngredient = choosingIngredientUiState.chosenIngredientDetails.toChoosenIngredient()
            val updatedListOfChosenIngredients = recipeUiState.createdRecipe.ingredients.toMutableList() ?: mutableListOf()
            updatedListOfChosenIngredients.add(convertedIngredient)

            val updatedCalories = recipeUiState.createdRecipe.totalCalories.toInt() + (calculateByWeight(
                convertedIngredient.ingredient?.calories ?: recipeUiState.createdRecipe.totalCalories.toInt(), convertedIngredient.weight))
            val updatedProteins = recipeUiState.createdRecipe.totalProtein.toInt() + (calculateByWeight(
                convertedIngredient.ingredient?.protein ?: recipeUiState.createdRecipe.totalProtein.toInt(), convertedIngredient.weight))
            val updatedFats = recipeUiState.createdRecipe.totalFats.toInt() + (calculateByWeight(
                convertedIngredient.ingredient?.fats ?: recipeUiState.createdRecipe.totalFats.toInt(), convertedIngredient.weight) )
            val updatedCarbs = recipeUiState.createdRecipe.totalCarbs.toInt() + (calculateByWeight(
                convertedIngredient.ingredient?.carbs ?: recipeUiState.createdRecipe.totalCarbs.toInt(), convertedIngredient.weight) )
            val updatedRecipe = recipeUiState.createdRecipe.copy(
                ingredients = updatedListOfChosenIngredients,
                ingredientCount = updatedListOfChosenIngredients.size.toString(),
                totalCalories = updatedCalories.toString(),
                totalFats = updatedFats.toString(),
                totalProtein = updatedProteins.toString(),
                totalCarbs = updatedCarbs.toString()
            )

            //Update Recipe UI
            updateRecipeUiState(updatedRecipe)
            //reset ui state of choosing ingredient
            updateChoosingUiState(ChoosenIngredientDetails())
        }
    }

    fun removeChosenIngredientFromRecipe(chosenIngredient: ChoosenIngredient) {
        val updatedListOfChosenIngredients = recipeUiState.createdRecipe.ingredients?.toMutableList()?.apply {
            remove(chosenIngredient)
        } ?: return

        val updatedCalories = recipeUiState.createdRecipe.totalCalories.toInt() - (calculateByWeight(
                chosenIngredient.ingredient?.calories ?: recipeUiState.createdRecipe.totalCalories.toInt(), chosenIngredient.weight) )
        val updatedProteins = recipeUiState.createdRecipe.totalProtein.toInt() - (calculateByWeight(
            chosenIngredient.ingredient?.protein ?: recipeUiState.createdRecipe.totalProtein.toInt(), chosenIngredient.weight) )
        val updatedFats = recipeUiState.createdRecipe.totalFats.toInt() - (calculateByWeight(
            chosenIngredient.ingredient?.fats ?: recipeUiState.createdRecipe.totalFats.toInt(), chosenIngredient.weight))
        val updatedCarbs = recipeUiState.createdRecipe.totalCarbs.toInt() - (calculateByWeight(
            chosenIngredient.ingredient?.carbs ?: recipeUiState.createdRecipe.totalCarbs.toInt(), chosenIngredient.weight) )
        val updatedRecipe = recipeUiState.createdRecipe.copy(
            ingredients = updatedListOfChosenIngredients,
            ingredientCount = updatedListOfChosenIngredients.size.toString(),
            totalCalories = updatedCalories.toString(),
            totalFats = updatedFats.toString(),
            totalProtein = updatedProteins.toString(),
            totalCarbs = updatedCarbs.toString()
        )

        updateRecipeUiState(updatedRecipe)
    }

    private fun validateChosenIngredient(): Boolean {
        return choosingIngredientUiState.chosenIngredientDetails.ingredient != null && choosingIngredientUiState.chosenIngredientDetails.weight.isNotBlank()
    }

    suspend fun saveRecipe() {
        if (validateRecipe()) {
            recipeRepository.insertRecipe(recipeUiState.createdRecipe.toRecipe())
        }
    }

}

data class RecipeUiState(
    val createdRecipe: RecipeDetails = RecipeDetails(),
    val isEntryValid: Boolean = false
)

data class ChoosingIngredientUiState(
    val chosenIngredientDetails: ChoosenIngredientDetails = ChoosenIngredientDetails(),
    val isEntryValid: Boolean = false
)

data class ChoosenIngredientDetails(
    val ingredient: Ingredient? = null,
    var weight: String = ""
)

data class RecipeDetails(
    val id: Int = 0,
    val name: String = "",
    val ingredients: MutableList<ChoosenIngredient> = mutableListOf(),
    val ingredientCount: String = "0",
    val totalCalories: String = "0",
    val totalProtein: String = "0",
    val totalFats: String = "0",
    val totalCarbs: String = "0"
)

fun ChoosenIngredientDetails.toChoosenIngredient() : ChoosenIngredient = ChoosenIngredient(
    ingredient = ingredient,
    weight = weight.toIntOrNull() ?: 0
)

fun RecipeDetails.toRecipe(): Recipe = Recipe(
    id = id,
    name = name,
    ingredients = ingredients.toList(),
    ingredientCount = ingredientCount.toIntOrNull() ?: 0,
    totalCalories = totalCalories.toIntOrNull() ?: 0,
    totalProtein = totalProtein.toIntOrNull() ?: 0,
    totalFats = totalFats.toIntOrNull() ?: 0,
    totalCarbs = totalCarbs.toIntOrNull() ?: 0
)
fun Recipe.toRecipeDetails(): RecipeDetails = RecipeDetails(
    id = id,
    name = name,
    ingredients = ingredients.toMutableList(),
    ingredientCount = ingredientCount.toString(),
    totalCalories = totalCalories.toString(),
    totalProtein = totalProtein.toString(),
    totalFats = totalFats.toString(),
    totalCarbs = totalCarbs.toString()
)

fun calculateByWeight(x:Int = 0, weight:Int): Int {
    return (x.toDouble() * (weight.toDouble()/100.0)).toInt()
}