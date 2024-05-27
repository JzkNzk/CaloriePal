package sk.fri.ballay10.caloriepal.roomdb.meal

import kotlinx.coroutines.flow.Flow
import sk.fri.ballay10.caloriepal.data.Meal

interface MealRepository {
    fun getAllMealsStream(): Flow<List<Meal>>

    fun getMealStream(id: Int): Flow<Meal?>

    suspend fun insertMeal(meal: Meal)

    suspend fun deleteMeal(meal: Meal)

    suspend fun updateMeal(meal: Meal)
}