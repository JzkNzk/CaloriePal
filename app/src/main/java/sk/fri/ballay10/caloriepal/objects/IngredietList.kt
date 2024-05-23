package sk.fri.ballay10.caloriepal.objects

import android.content.Context
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import sk.fri.ballay10.caloriepal.R
import sk.fri.ballay10.caloriepal.data.Ingredient
import java.io.InputStream

object IngredietList {

    private lateinit var appContext: Context

    // Initialize the context
    fun initializeIngredientListContext(context: Context) {
        this.appContext = context.applicationContext
    }

    val ingredients by lazy {
        parseIngredients(appContext)
    }

    // Function to parse ingredients from the XML file
    private fun parseIngredients(context: Context): List<Ingredient> {
        val ingredients = mutableListOf<Ingredient>()

        val parser: XmlPullParser = context.resources.getXml(R.xml.ingredients)

        var eventType = parser.eventType
        var currentTag: String? = null
        var name: String? = null
        var imageRes = 0
        var calories = 0
        var protein = 0
        var fat = 0
        var carbs = 0
        var baseWeight = 0

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    currentTag = parser.name
                    if (currentTag == "ingredient") {
                        // Reset values for each ingredient
                        name = null
                        imageRes = 0
                        calories = 0
                        protein = 0
                        fat = 0
                        carbs = 0
                        baseWeight = 0
                    }
                }
                XmlPullParser.TEXT -> {
                    val text = parser.text
                    if (!text.isNullOrBlank() && currentTag != null) {
                        when (currentTag) {
                            "name" -> name = text
                            "image" -> {
                                // Get the drawable resource ID directly
                                val resId = context.resources.getIdentifier(text, "drawable", context.packageName)
                                if (resId != 0) {
                                    imageRes = resId
                                } else {
                                    throw IllegalArgumentException("Drawable resource not found: $text")
                                }
                            }
                            "calories" -> calories = text.toInt()
                            "protein" -> protein = text.toInt()
                            "fat" -> fat = text.toInt()
                            "carbs" -> carbs = text.toInt()
                            "base_weight" -> baseWeight = text.toInt()
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (parser.name == "ingredient" && name != null) {
                        val ingredient = Ingredient(name, imageRes, calories, protein, fat, carbs, baseWeight)
                        ingredients.add(ingredient)
                    }
                    currentTag = null
                }
            }
            eventType = parser.next()
        }

        return ingredients.sortedBy { it.name }
    }
}