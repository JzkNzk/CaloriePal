package sk.fri.ballay10.caloriepal.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.fri.ballay10.caloriepal.data.Meal
import sk.fri.ballay10.caloriepal.data.NutrientSummary
import sk.fri.ballay10.caloriepal.objects.CalendarProvider
import sk.fri.ballay10.caloriepal.roomdb.summary.SummaryRepository

class SummaryPageViewModel(private val summaryRepository: SummaryRepository) : ViewModel() {

    var summaryUiState by mutableStateOf(SummaryUiState())
        private set
    init {
        viewModelScope.launch {
            if (checkIfSummaryExists(CalendarProvider.todayId)) {
                val collectedSummary = summaryRepository.getSummaryStream(CalendarProvider.todayId)
                    .filterNotNull()
                    .first().toSummaryDetails()
                summaryUiState = SummaryUiState(displayedSummary = collectedSummary)
            } else {
                summaryUiState = SummaryUiState()
            }
        }
    }
    suspend fun updateSummaryUiStateDay(summaryUiStateCopy: SummaryUiState) {
        val day = summaryUiStateCopy.selectedDay.toInt()
        val month = summaryUiStateCopy.selectedMonth.toInt() + 1
        val year = summaryUiStateCopy.selectedYear.toInt()
        viewModelScope.launch {
            if (checkIfSummaryExists(selectedDateToId(day, month, year))) {
                val collectedSummary = summaryRepository.getSummaryStream(selectedDateToId(day, month, year))
                    .filterNotNull()
                    .first().toSummaryDetails()
                summaryUiState = SummaryUiState(
                    displayedSummary = collectedSummary,
                    selectedDay = day.toString(),
                    selectedMonth = month.toString(),
                    selectedYear = year.toString())
            } else {
                summaryUiState = SummaryUiState(
                    displayedSummary = SummaryDetails(id = selectedDateToId(day, month, year).toString()),
                    selectedDay = day.toString(),
                    selectedMonth = month.toString(),
                    selectedYear = year.toString())
            }
        }

    }
    suspend fun updateSummaryUiStateStatistics(updatedSummary: SummaryDetails) {
        val newSummary = SummaryDetails(
            id = updatedSummary.id,
            setCalories = updatedSummary.setCalories,
            consumedCalories = (updatedSummary.consumedCalories.toInt() + summaryUiState.displayedSummary.consumedCalories.toInt()).toString(),
            consumedFats = (updatedSummary.consumedFats.toInt() + summaryUiState.displayedSummary.consumedFats.toInt()).toString(),
            consumedCarbs = (updatedSummary.consumedCarbs.toInt() + summaryUiState.displayedSummary.consumedCarbs.toInt()).toString(),
            consumedProteins = (updatedSummary.consumedProteins.toInt() + summaryUiState.displayedSummary.consumedProteins.toInt()).toString()
        )
        summaryUiState = summaryUiState.copy(displayedSummary = newSummary)
        val currentId = summaryUiState.displayedSummary.id.toInt()
        if (checkIfSummaryExists(currentId)) {
            updateSummary()
        }
        else {
            saveSummary()
        }
    }


    private suspend fun checkIfSummaryExists(id: Int):Boolean {
        return summaryRepository.getSummaryStream(id)
            .firstOrNull() != null
    }

    private fun selectedDateToId(day:Int, month:Int, year:Int): Int {
        return (day) + ((month)*100) + (year*10000)
    }

    private suspend fun saveSummary() {
        summaryRepository.insertSummary(summaryUiState.displayedSummary.toNutrientSummary())
    }

    private suspend fun updateSummary() {
        summaryRepository.updateSummary(summaryUiState.displayedSummary.toNutrientSummary())
    }

}

data class SummaryUiState(
    val displayedSummary: SummaryDetails = SummaryDetails(),
    val selectedDay: String = CalendarProvider.calendar.get(android.icu.util.Calendar.DAY_OF_MONTH).toString(),
    val selectedMonth: String = CalendarProvider.calendar.get(android.icu.util.Calendar.MONTH).toString(),
    val selectedYear: String = CalendarProvider.calendar.get(android.icu.util.Calendar.YEAR).toString()
)

data class SummaryDetails(
    val id : String = "${CalendarProvider.todayId}",
    val setCalories :String = "0",
    val consumedCalories :String = "0",
    val consumedFats : String = "0",
    val consumedProteins : String = "0",
    val consumedCarbs : String = "0"
)

fun NutrientSummary.toSummaryDetails(): SummaryDetails = SummaryDetails(
    id = id.toString(),
    setCalories = setCalories.toString(),
    consumedCalories = consumedCalories.toString(),
    consumedFats = consumedFats.toString(),
    consumedCarbs = consumedCarbs.toString(),
    consumedProteins = consumedProteins.toString()
)

fun SummaryDetails.toNutrientSummary(): NutrientSummary = NutrientSummary(
    id = id.toInt(),
    setCalories = setCalories.toInt(),
    consumedCalories = consumedCalories.toInt(),
    consumedFats = consumedFats.toInt(),
    consumedCarbs = consumedCarbs.toInt(),
    consumedProteins = consumedProteins.toInt()
)
