package sk.fri.ballay10.caloriepal.roomdb.summary

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import sk.fri.ballay10.caloriepal.data.NutrientSummary

//Database builder class for Summaries
@Database(entities = [NutrientSummary::class], version = 1, exportSchema = false)
abstract class SummaryDatabase : RoomDatabase() {

    abstract fun summaryDao(): SummaryDao

    companion object {
        @Volatile
        private var Instance: SummaryDatabase? = null

        fun getDatabase(context: Context): SummaryDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, SummaryDatabase::class.java, "summary_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}