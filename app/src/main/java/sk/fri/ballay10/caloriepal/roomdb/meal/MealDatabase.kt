package sk.fri.ballay10.caloriepal.roomdb.meal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import sk.fri.ballay10.caloriepal.data.Meal
import sk.fri.ballay10.caloriepal.roomdb.Converters


//Database builder class for Meals
@Database(entities = [Meal::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MealDatabase : RoomDatabase() {

    abstract fun mealDao(): MealDao

    companion object {
        @Volatile
        private var Instance: MealDatabase? = null

        fun getDatabase(context: Context): MealDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MealDatabase::class.java, "meal_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}