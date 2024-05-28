package sk.fri.ballay10.caloriepal.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import sk.fri.ballay10.caloriepal.data.Meal
import sk.fri.ballay10.caloriepal.data.Recipe
import sk.fri.ballay10.caloriepal.roomdb.meal.MealRepository
import sk.fri.ballay10.caloriepal.roomdb.recipe.RecipeRepository
import sk.fri.ballay10.caloriepal.ui.theme.colorBlue1
import sk.fri.ballay10.caloriepal.ui.theme.colorRed1

class MealsAndRecipesViewModel(private val recipeRepository: RecipeRepository, private val mealRepository: MealRepository) : ViewModel() {

    var recipeListUiState: StateFlow<RecipeListUiState> =
        recipeRepository.getAllRecipesStream()
            .filterNotNull()
            .map { RecipeListUiState(recipeList = it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = RecipeListUiState()
            )

    val mealListUiState: StateFlow<MealListUiState> =
        mealRepository.getAllMealsStream()
            .filterNotNull()
            .map { MealListUiState(mealList = it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MealListUiState()
            )

    var pageUiState by mutableStateOf(PageUiState())
        private set

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun updatePageUiState(page: Int) {
        pageUiState = if (page == 0) {
            PageUiState(color = colorBlue1, currentPage = page)
        } else {
            PageUiState(color = colorRed1, currentPage = page)
        }
    }

    suspend fun deleteRecipe(recipe: Recipe) {
        recipeRepository.deleteRecipe(recipe)
    }

    suspend fun deleteMeal(meal: Meal) {
        mealRepository.deleteMeal(meal)
    }

}

data class RecipeListUiState(
    val recipeList: List<Recipe> = listOf(),
)

data class MealListUiState(
    val mealList: List<Meal> = listOf(),
)

data class PageUiState(
    val color: Color = colorBlue1,
    val currentPage: Int = 0,
)
