package sk.fri.ballay10.caloriepal.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import sk.fri.ballay10.caloriepal.data.Ingredient
import sk.fri.ballay10.caloriepal.objects.IngredietList
import sk.fri.ballay10.caloriepal.ui.theme.TopDescriptionBar

@Composable
fun IngredientScreen() {
    val ingredients = IngredietList.ingredients
    Scaffold(
        topBar = {
            TopDescriptionBar("INGREDIENTS")
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(ingredients) { ingredient ->
                IngredientItem(ingredient = ingredient)
            }
        }
    }
}



@Composable
fun IngredientItem(ingredient: Ingredient) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { expanded = !expanded }
        .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = ingredient.imageRes),
                contentDescription = ingredient.name,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = ingredient.name, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.weight(1f))
            if(!expanded) {
                Icon(
                    Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Collapsed",
                )
            } else {
                Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Expanded" )
            }
        }
        if (expanded) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.LightGray)
                .padding(8.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Calories: ${ingredient.calories}", style = MaterialTheme.typography.body2, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(

                    text = "Protein: ${ingredient.protein}g, Fats: ${ingredient.fats}g, Carbs: ${ingredient.carbs}g",
                    style = MaterialTheme.typography.body2
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Weight: ${ingredient.weight}g"
                )
            }
        }
        Divider()
    }
}