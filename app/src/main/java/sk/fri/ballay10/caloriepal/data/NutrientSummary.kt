package sk.fri.ballay10.caloriepal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NutrientSummary(
    @PrimaryKey(autoGenerate = false)
    val id : Int,
    val setCalories :Int = 0,
    val consumedCalories :Int = 0,
    val consumedFats : Int = 0,
    val consumedProteins : Int = 0,
    val consumetCarbs : Int = 0
)
