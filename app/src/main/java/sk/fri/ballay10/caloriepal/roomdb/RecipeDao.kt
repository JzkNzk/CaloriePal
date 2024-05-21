package sk.fri.ballay10.caloriepal.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import sk.fri.ballay10.caloriepal.data.Recipe

@Dao
interface RecipeDao {
    @Query("SELECT * FROM RECIPE")
    fun getAllRecipes() : LiveData<List<Recipe>>

    @Insert
    fun addRecipe(recipe : Recipe)

    @Query("Delete FROM Recipe where id = :id")
    fun deleteRecipe(id : Int)
}