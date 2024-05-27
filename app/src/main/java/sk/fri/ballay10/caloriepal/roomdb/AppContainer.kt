package sk.fri.ballay10.caloriepal.roomdb

import android.content.Context
import sk.fri.ballay10.caloriepal.roomdb.meal.OfflineMealRepository
import sk.fri.ballay10.caloriepal.roomdb.meal.MealDatabase
import sk.fri.ballay10.caloriepal.roomdb.meal.MealRepository
import sk.fri.ballay10.caloriepal.roomdb.recipe.OfflineRecipeRepository
import sk.fri.ballay10.caloriepal.roomdb.recipe.RecipeDatabase
import sk.fri.ballay10.caloriepal.roomdb.recipe.RecipeRepository
import sk.fri.ballay10.caloriepal.roomdb.summary.SummaryRepository
import sk.fri.ballay10.caloriepal.roomdb.summary.SummaryDatabase
import sk.fri.ballay10.caloriepal.roomdb.summary.OfflineSummaryRepository


/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val recipeRepository: RecipeRepository
    val mealRepository: MealRepository
    val summaryRepository: SummaryRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineRecipeRepository], [OfflineMealRepository], [OfflineSummaryRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [RecipeRepository]
     */
    override val recipeRepository: RecipeRepository by lazy {
        OfflineRecipeRepository(RecipeDatabase.getDatabase(context).recipeDao())
    }
    /**
     * Implementation for [MealRepository]
     */
    override val mealRepository: MealRepository by lazy {
        OfflineMealRepository(MealDatabase.getDatabase(context).mealDao())
    }
    /**
     * Implementation for [SummaryRepository]
     */
    override val summaryRepository: SummaryRepository by lazy {
        OfflineSummaryRepository(SummaryDatabase.getDatabase(context).summaryDao())
    }
}