package sk.fri.ballay10.caloriepal.roomdb.meal

import kotlinx.coroutines.flow.Flow
import sk.fri.ballay10.caloriepal.data.Meal


class OfflineMealRepository(private val mealDao: MealDao) : MealRepository {
    override fun getAllMealsStream(): Flow<List<Meal>> = mealDao.getAllItems()

    override fun getMealStream(id: Int): Flow<Meal?> = mealDao.getItem(id)

    override suspend fun insertMeal(meal: Meal) = mealDao.insert(meal)

    override suspend fun deleteMeal(meal: Meal) = mealDao.delete(meal)

    override suspend fun updateMeal(meal: Meal) = mealDao.update(meal)

}