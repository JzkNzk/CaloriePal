package sk.fri.ballay10.caloriepal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val ingredients: MutableList<ChoosenIngredient>,
    val ingredientCount: Int,
    val totalCalories: Int,
    val totalProtein: Int,
    val totalFats: Int,
    val totalCarbs: Int
)
