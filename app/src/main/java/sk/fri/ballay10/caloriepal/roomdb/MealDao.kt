package sk.fri.ballay10.caloriepal.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import sk.fri.ballay10.caloriepal.data.Meal

@Dao
interface MealDao {
    @Query("SELECT * FROM MEAL")
    fun getAllMeals() : LiveData<List<Meal>>

    @Insert
    fun addMeal(meal : Meal)

    @Query("Delete FROM Meal where id = :id")
    fun deleteMeal(id : Int)
}