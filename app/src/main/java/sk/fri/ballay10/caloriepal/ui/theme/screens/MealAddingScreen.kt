package sk.fri.ballay10.caloriepal.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigamole.composeshadowsplus.common.shadowsPlus
import sk.fri.ballay10.caloriepal.R
import sk.fri.ballay10.caloriepal.ui.theme.ActionButtons
import sk.fri.ballay10.caloriepal.ui.theme.TopDescriptionBar
import sk.fri.ballay10.caloriepal.ui.theme.EnterNameForm
import sk.fri.ballay10.caloriepal.ui.theme.colorBlue1
import sk.fri.ballay10.caloriepal.ui.theme.colorBlue2

@Composable
fun MealAddingScreen(modifier: Modifier = Modifier) {
    var isPickDialogVisible by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold (
        topBar = { TopDescriptionBar(title = "Add meal", bgColor = colorBlue1) },
        bottomBar = {}

    ){
        Column (
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues = it)
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)

        ) {
            EnterNameForm(nameOfWhat = stringResource(id = R.string.meal_lowercase))
            Spacer(modifier = modifier.height(16.dp))
            PickedRecipeList(onAddRecipe = {
                isPickDialogVisible = true
            })
            ActionButtons()
        }
        if (isPickDialogVisible) {
            PickRecipeDialog(onCancel = {isPickDialogVisible = false})
        }
    }
}

@Preview
@Composable
fun MealAddingScreenPreview(modifier: Modifier = Modifier) {
    MealAddingScreen()
}

@Composable
fun PickedRecipeList(onAddRecipe: () -> Unit,modifier: Modifier = Modifier) {
    Column (
        modifier = modifier
            .shadowsPlus()
            .fillMaxWidth()
            .height(400.dp)
            .background(color = Color.White, shape = RoundedCornerShape(15.dp))
            .padding(8.dp)
    ){
        Text(text = "Ingredient list", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            item {
                RecipeEntry()
            }
            item {
                RecipeEntry()
            }
            item {
                RecipeEntry()
            }
        }
        Divider()
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Column {
                Row {
                    Text(text = "Total calories: ", fontWeight = FontWeight.Bold)
                    Text(text = "1000")
                }
                Row {
                    Text(text = "Protein: ", fontWeight = FontWeight.Bold)
                    Text(text = "10g, ")

                    Text(text = "Fats: ", fontWeight = FontWeight.Bold)
                    Text(text = "10g, ")

                    Text(text = "Carbs: ", fontWeight = FontWeight.Bold)
                    Text(text = "10g ")
                }
            }
            Button(onClick = onAddRecipe) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    }
}

@Composable
fun RecipeEntry() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(painter = painterResource(id = R.drawable.baseline_sticky_note_2_24), contentDescription = "Recipe" )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Recipe #1", style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "100 kcal")
            IconButton(onClick = {}) {
                Icon(Icons.Default.Close, contentDescription = "Remove")
            }
        }
        Divider()
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeEntryPrev() {
    RecipeEntry()
}

@Composable
fun PickRecipeDialog(onCancel: () -> Unit) {
    var selectedOption by rememberSaveable { mutableStateOf("") }
    var inputWeight by rememberSaveable { mutableStateOf("") }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(text = "Pick a recipe")
        },
        text = {
            Column {
                LazyColumn(
                    modifier = Modifier.height(300.dp)
                ) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                }
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "option")
                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "100 kcal")
                                Text(text = "21 ingredients", fontSize = 11.sp)
                            }

                            RadioButton(
                                selected = (true),
                                onClick = {

                                }
                            )
                        }
                    }
                }
                Divider()
            }


        },
        confirmButton = {
            TextButton(onClick = {}) {
                Text("Pick")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
        },

        )
}

@Preview(showBackground = true)
@Composable
fun PickRecipeDialogPreview() {
    PickRecipeDialog(onCancel = {})
}