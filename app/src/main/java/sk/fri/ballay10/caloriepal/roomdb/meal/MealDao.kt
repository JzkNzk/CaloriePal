package sk.fri.ballay10.caloriepal.roomdb.meal

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import sk.fri.ballay10.caloriepal.data.Meal

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(meal: Meal)

    @Update
    suspend fun update(meal: Meal)

    @Delete
    suspend fun delete(meal: Meal)

    @Query("SELECT * from meals WHERE id = :id")
    fun getItem(id: Int): Flow<Meal>

    @Query("SELECT * from meals ORDER BY name ASC")
    fun getAllItems(): Flow<List<Meal>>
}