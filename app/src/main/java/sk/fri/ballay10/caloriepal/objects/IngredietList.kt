package sk.fri.ballay10.caloriepal.objects

import sk.fri.ballay10.caloriepal.R
import sk.fri.ballay10.caloriepal.data.Ingredient

object IngredietList {
    //Stored ingredients
    val storedIngredients: List<Ingredient> = listOf(
        Ingredient("Apple", R.drawable.apple, 52, 0, 0, 14, 100),
        Ingredient("Banana", R.drawable.banana, 89, 1, 0, 23, 100),
        Ingredient("Blueberry", R.drawable.blueberries, 57, 1, 0, 14, 100),
        Ingredient("Cabbage", R.drawable.cabbage, 24, 1,0,6,100),
        Ingredient("Cheese", R.drawable.cheese, 402, 25,33,0,100),
        Ingredient("Chicken Breast", R.drawable.chickenbreast, 164, 31, 4, 0, 100),
        Ingredient("Cucumber", R.drawable.cucumber, 15,1,0,4,100),
        Ingredient("Garlic", R.drawable.garlic, 149, 6, 0, 33, 100),
        Ingredient("Mozzarella", R.drawable.mozzarella, 210, 28,17,3,100),
        Ingredient("Oatmeal", R.drawable.oatmeal, 389, 17,7,66,100),
        Ingredient("Onion", R.drawable.onion, 42,1,0,10,100),
        Ingredient("Orange", R.drawable.orange, 47, 1, 0, 12, 100),
        Ingredient("Pasta", R.drawable.pasta, 131, 5,1,25,100),
        Ingredient("Pork", R.drawable.pork, 242, 27,14,0,100),
        Ingredient("Potato", R.drawable.potato, 77,2,0,17,100),
        Ingredient("Rice", R.drawable.rice, 135, 3,1,27,100),
        Ingredient("Beef", R.drawable.steak, 134, 21,5,0,100),
        Ingredient("Strawberry", R.drawable.strawberry, 32, 1, 0, 8, 100),
        Ingredient("Tomato", R.drawable.tomato, 18,1,0,4,100)
    ).sortedBy { it.name }
}