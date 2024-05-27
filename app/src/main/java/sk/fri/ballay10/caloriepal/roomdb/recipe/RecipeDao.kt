package sk.fri.ballay10.caloriepal.roomdb.recipe

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import sk.fri.ballay10.caloriepal.data.Recipe

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recipe: Recipe)

    @Update
    suspend fun update(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("SELECT * from recipes WHERE id = :id")
    fun getItem(id: Int): Flow<Recipe>

    @Query("SELECT * from recipes ORDER BY name ASC")
    fun getAllItems(): Flow<List<Recipe>>
}