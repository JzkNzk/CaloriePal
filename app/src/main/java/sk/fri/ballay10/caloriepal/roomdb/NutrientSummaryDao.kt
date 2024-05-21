package sk.fri.ballay10.caloriepal.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import sk.fri.ballay10.caloriepal.data.NutrientSummary

@Dao
interface NutrientSummaryDao {
    @Query("SELECT * FROM NUTRIENTSUMMARY")
    fun getAllSummaries(): LiveData<List<NutrientSummary>>

    @Insert
    fun insertSummary(summary: NutrientSummary)

    @Update
    fun updateSummary(summary: NutrientSummary)

    @Query("SELECT * FROM NUTRIENTSUMMARY WHERE id = :id")
    fun getSummaryByIdSync(id: Int): NutrientSummary?

    @Query("SELECT * FROM NUTRIENTSUMMARY WHERE id = :id")
    fun getSummaryById(id: Int): LiveData<NutrientSummary?>
}