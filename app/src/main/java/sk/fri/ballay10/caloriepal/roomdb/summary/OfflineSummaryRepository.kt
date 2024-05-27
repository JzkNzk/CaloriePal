package sk.fri.ballay10.caloriepal.roomdb.summary

import kotlinx.coroutines.flow.Flow
import sk.fri.ballay10.caloriepal.data.NutrientSummary


class OfflineSummaryRepository(private val summaryDao: SummaryDao) : SummaryRepository {
    override fun getAllSummariesStream(): Flow<List<NutrientSummary>> = summaryDao.getAllItems()

    override fun getSummaryStream(id: Int): Flow<NutrientSummary?> = summaryDao.getItem(id)

    override suspend fun insertSummary(summary: NutrientSummary) = summaryDao.insert(summary)

    override suspend fun deleteSummary(summary: NutrientSummary) = summaryDao.delete(summary)

    override suspend fun updateSummary(summary: NutrientSummary) = summaryDao.update(summary)

}