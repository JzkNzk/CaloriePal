package sk.fri.ballay10.caloriepal.roomdb.summary

import kotlinx.coroutines.flow.Flow
import sk.fri.ballay10.caloriepal.data.NutrientSummary

interface SummaryRepository {
    fun getAllSummariesStream(): Flow<List<NutrientSummary>>

    fun getSummaryStream(id: Int): Flow<NutrientSummary?>

    suspend fun insertSummary(summary: NutrientSummary)

    suspend fun deleteSummary(summary: NutrientSummary)

    suspend fun updateSummary(summary: NutrientSummary)
}