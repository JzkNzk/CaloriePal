package sk.fri.ballay10.caloriepal.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import sk.fri.ballay10.caloriepal.data.NutrientSummary

@Database(entities = [NutrientSummary::class], version = 1)
abstract class NutrientSummaryDatabase : RoomDatabase() {

    companion object {
        const val NAME = "NutrientSummary_DB"
    }

    abstract fun getNutrientSummaryDao() : NutrientSummaryDao

}