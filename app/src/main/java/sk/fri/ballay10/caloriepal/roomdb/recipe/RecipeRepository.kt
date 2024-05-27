package sk.fri.ballay10.caloriepal.roomdb.recipe

import kotlinx.coroutines.flow.Flow
import sk.fri.ballay10.caloriepal.data.Recipe

interface RecipeRepository {
    fun getAllRecipesStream(): Flow<List<Recipe>>

    fun getRecipeStream(id: Int): Flow<Recipe?>

    suspend fun insertRecipe(recipe: Recipe)

    suspend fun deleteRecipe(recipe: Recipe)

    suspend fun updateRecipe(recipe: Recipe)
}