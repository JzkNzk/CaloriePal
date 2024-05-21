package sk.fri.ballay10.caloriepal.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import sk.fri.ballay10.caloriepal.data.Recipe

@Database(entities = [Recipe::class], version = 1)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {

    companion object {
        const val NAME = "Recipe_DB"
    }

    abstract fun getRecipeDao() : RecipeDao

}