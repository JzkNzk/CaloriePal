package sk.fri.ballay10.caloriepal.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val meals: List<ChoosenRecipe>,
    val recipeCount: Int,
    val totalCalories: Int,
    val totalProtein: Int,
    val totalFats: Int,
    val totalCarbs: Int
)
