package sk.fri.ballay10.caloriepal.roomdb

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sk.fri.ballay10.caloriepal.data.ChoosenIngredient
import sk.fri.ballay10.caloriepal.data.Recipe

class Converters {

    @TypeConverter
    fun fromChoosenIngredientList(value: List<ChoosenIngredient>): String {
        val gson = Gson()
        val type = object : TypeToken<List<ChoosenIngredient>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toChoosenIngredientList(value: String): List<ChoosenIngredient> {
        val gson = Gson()
        val type = object : TypeToken<List<ChoosenIngredient>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromChoosenRecipeList(value: List<Recipe>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Recipe>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toChoosenRecipeList(value: String): List<Recipe> {
        val gson = Gson()
        val type = object : TypeToken<List<Recipe>>() {}.type
        return gson.fromJson(value, type)
    }
}