package sk.fri.ballay10.caloriepal.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import sk.fri.ballay10.caloriepal.data.Meal

@Database(entities = [Meal::class], version = 1)
@TypeConverters(Converters::class)
abstract class MealDatabase : RoomDatabase() {

    companion object {
        const val NAME = "Meal_DB"
    }

    abstract fun getMealDao() : MealDao

}