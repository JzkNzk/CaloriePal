package sk.fri.ballay10.caloriepal.viewModels

import androidx.lifecycle.ViewModel
import sk.fri.ballay10.caloriepal.roomdb.summary.SummaryRepository

class SummaryPageViewModel(private val summaryRepository: SummaryRepository) : ViewModel() {
    suspend fun saveSummary() {
        //recipeRepository.insertRecipe()
    }
}