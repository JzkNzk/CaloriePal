package sk.fri.ballay10.caloriepal.roomdb.summary

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import sk.fri.ballay10.caloriepal.data.NutrientSummary

@Dao
interface SummaryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(nutrientSummary: NutrientSummary)

    @Update
    suspend fun update(nutrientSummary: NutrientSummary)

    @Delete
    suspend fun delete(nutrientSummary: NutrientSummary)

    @Query("SELECT * from summaries WHERE id = :id")
    fun getItem(id: Int): Flow<NutrientSummary>

    @Query("SELECT * from summaries")
    fun getAllItems(): Flow<List<NutrientSummary>>
}