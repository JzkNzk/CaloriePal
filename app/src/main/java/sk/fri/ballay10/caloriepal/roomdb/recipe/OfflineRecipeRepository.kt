package sk.fri.ballay10.caloriepal.roomdb.recipe

import kotlinx.coroutines.flow.Flow
import sk.fri.ballay10.caloriepal.data.Recipe

class OfflineRecipeRepository(private val recipeDao: RecipeDao) : RecipeRepository {
    override fun getAllRecipesStream(): Flow<List<Recipe>> = recipeDao.getAllItems()

    override fun getRecipeStream(id: Int): Flow<Recipe?> = recipeDao.getItem(id)

    override suspend fun insertRecipe(recipe: Recipe) = recipeDao.insert(recipe)

    override suspend fun deleteRecipe(recipe: Recipe) = recipeDao.delete(recipe)

    override suspend fun updateRecipe(recipe: Recipe) = recipeDao.update(recipe)

}