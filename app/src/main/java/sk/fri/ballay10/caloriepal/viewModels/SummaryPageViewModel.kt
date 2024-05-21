package sk.fri.ballay10.caloriepal.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.fri.ballay10.caloriepal.MainActivity
import sk.fri.ballay10.caloriepal.data.Meal
import sk.fri.ballay10.caloriepal.data.NutrientSummary

class SummaryPageViewModel : ViewModel() {
    private val _nutrientSummaryDao = MainActivity.nutrientSummaryDatabase.getNutrientSummaryDao()

    private fun addNutrientSummary(summary: NutrientSummary) {
        //Insert summary if it does not exist yet
        //If it exist just update the existing one
        viewModelScope.launch(Dispatchers.IO) {
            val existingSummary = getSummaryByIdSync(summary.id)
            if (existingSummary == null) {
                _nutrientSummaryDao.insertSummary(summary)
            } else {
                updateNutrientSummary(summary)
            }
        }
    }

    private fun updateNutrientSummary(summary: NutrientSummary) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingSummary = getSummaryByIdSync(summary.id)
            var updatedSummary : NutrientSummary? = null
            //Update summary based on provided summary
            if (summary.setCalories != 0) {
                if (existingSummary != null) {
                    updatedSummary =
                        NutrientSummary(
                            id = existingSummary.id,
                            setCalories = summary.setCalories,
                            consumedCalories = summary.consumedCalories + existingSummary.consumedCalories,
                            consumedFats = summary.consumedFats + existingSummary.consumedFats,
                            consumedProteins = summary.consumedProteins + existingSummary.consumedProteins,
                            consumetCarbs = summary.consumetCarbs + existingSummary.consumetCarbs
                        )
                }
            } else {
                if (existingSummary != null) {
                    updatedSummary =
                        NutrientSummary(
                            id = existingSummary.id,
                            setCalories = existingSummary.setCalories,
                            consumedCalories = summary.consumedCalories + existingSummary.consumedCalories,
                            consumedFats = summary.consumedFats + existingSummary.consumedFats,
                            consumedProteins = summary.consumedProteins + existingSummary.consumedProteins,
                            consumetCarbs = summary.consumetCarbs + existingSummary.consumetCarbs
                        )
                }
            }

            if (updatedSummary != null) {
                _nutrientSummaryDao.updateSummary(updatedSummary)
            }
        }
    }

    private suspend fun getSummaryByIdSync(id: Int): NutrientSummary? {
        return withContext(Dispatchers.IO) {
            _nutrientSummaryDao.getSummaryByIdSync(id)
        }
    }

    fun getSummaryById(id: Int): LiveData<NutrientSummary?> {
        return _nutrientSummaryDao.getSummaryById(id)
    }

    // Processes summary based on information given
    fun processSummary(meal: Meal? = null, goal: Int = 0, id: Int) {
        if (meal == null) {
            val nutrientSummary = NutrientSummary(id = id, setCalories = goal)
            addNutrientSummary(nutrientSummary)
        } else {
            val nutrientSummary = NutrientSummary(
                id = id,
                consumedCalories = meal.totalCalories,
                consumedFats = meal.totalFats,
                consumedProteins = meal.totalProtein,
                consumetCarbs = meal.totalCarbs
            )
            addNutrientSummary(nutrientSummary)
        }

    }

}