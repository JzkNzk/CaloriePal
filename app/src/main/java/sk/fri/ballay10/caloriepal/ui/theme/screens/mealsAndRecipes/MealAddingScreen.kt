package sk.fri.ballay10.caloriepal.ui.theme.screens.mealsAndRecipes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gigamole.composeshadowsplus.common.shadowsPlus
import kotlinx.coroutines.launch
import sk.fri.ballay10.caloriepal.R
import sk.fri.ballay10.caloriepal.data.Recipe
import sk.fri.ballay10.caloriepal.ui.theme.ActionButtons
import sk.fri.ballay10.caloriepal.ui.theme.TopDescriptionBar
import sk.fri.ballay10.caloriepal.ui.theme.EnterNameForm
import sk.fri.ballay10.caloriepal.ui.theme.colorBlue1
import sk.fri.ballay10.caloriepal.ui.theme.screens.AppViewModelProvider
import sk.fri.ballay10.caloriepal.viewModels.ChoosenRecipeDetails
import sk.fri.ballay10.caloriepal.viewModels.ChoosenRecipeUiState
import sk.fri.ballay10.caloriepal.viewModels.MealAddingViewModel
import sk.fri.ballay10.caloriepal.viewModels.MealUiState

@Composable
fun MealAddingScreen(viewModel: MealAddingViewModel = viewModel(factory = AppViewModelProvider.Factory), modifier: Modifier = Modifier, returnBack: () -> Unit) {
    var isPickDialogVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val recipeList by viewModel.recipeList.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val verticalScroll = rememberScrollState()

    Scaffold (
        topBar = { TopDescriptionBar(title = "Add meal", bgColor = colorBlue1) },
        bottomBar = {}

    ){
        Column (
            modifier = modifier
                .verticalScroll(verticalScroll)
                .fillMaxSize()
                .padding(paddingValues = it)
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 0.dp)


        ) {
            EnterNameForm(
                nameOfWhat = stringResource(id = R.string.meal_lowercase),
                onNameChange = {name ->
                    viewModel.updateMealUiState(viewModel.mealUiState.createdMeal.copy(name = name))
                },
                value = viewModel.mealUiState.createdMeal.name)
            Spacer(modifier = modifier.height(16.dp))
            PickedRecipeList(
                onAddRecipe = { isPickDialogVisible = true },
                removeRecipe = { recipe -> viewModel.removeChosenRecepieFromMeal(recipe) },
                mealUiState = viewModel.mealUiState
                )
            ActionButtons(onCancel = returnBack, onConfirm = {
                coroutineScope.launch {
                    viewModel.saveMeal()
                }

                returnBack()
            })
        }
        if (isPickDialogVisible) {
            PickRecipeDialog(
                onCancel = {isPickDialogVisible = false},
                choosingRecipeUiState = viewModel.choosingRecipeUiState,
                recipes = recipeList,
                onRecipeChange = viewModel::updateChoosingUiState,
                onConfirm = {
                    viewModel.addChosenRecipeToMeal()
                    isPickDialogVisible = false
                }
            )
        }
    }
}

@Composable
fun PickedRecipeList(onAddRecipe: () -> Unit,
                     modifier: Modifier = Modifier,
                     mealUiState: MealUiState,
                     removeRecipe: (Recipe) -> Unit) {
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
            items(mealUiState.createdMeal.recipes){recipe ->
                RecipeEntry(choosenRecipe = recipe, onRemove = {removeRecipe(recipe)})
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
                    Text(text = "${mealUiState.createdMeal.totalCalories} kcal")
                }
                Row {
                    Text(text = "Protein: ", fontWeight = FontWeight.Bold)
                    Text(text = "${mealUiState.createdMeal.totalProtein}g, ")

                    Text(text = "Fats: ", fontWeight = FontWeight.Bold)
                    Text(text = "${mealUiState.createdMeal.totalFats}g, ")

                    Text(text = "Carbs: ", fontWeight = FontWeight.Bold)
                    Text(text = "${mealUiState.createdMeal.totalCarbs}g ")
                }
            }
            Button(onClick = onAddRecipe) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    }
}

@Composable
fun RecipeEntry(choosenRecipe: Recipe, onRemove: () -> Unit) {
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
            Text(text = choosenRecipe.name, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "${choosenRecipe.totalCalories} kcal")
            IconButton(onClick = {onRemove()}) {
                Icon(Icons.Default.Close, contentDescription = "Remove")
            }
        }
        Divider()
    }
}

@Composable
fun PickRecipeDialog(
    onCancel: () -> Unit,
    choosingRecipeUiState: ChoosenRecipeUiState,
    recipes: List<Recipe>,
    onRecipeChange: (ChoosenRecipeDetails) -> Unit,
    onConfirm: () -> Unit
) {

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
                    items(recipes) {recipe ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                }
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = recipe.name)
                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "${recipe.totalCalories} kcal")
                                Text(text = "${recipe.ingredientCount} ingredients", fontSize = 11.sp)
                            }

                            RadioButton(
                                selected = recipe.id == (choosingRecipeUiState.chosenRecipeDetails.recipe?.id
                                    ?: false),
                                onClick = {
                                    onRecipeChange(choosingRecipeUiState.chosenRecipeDetails.copy(recipe = recipe))
                                }
                            )
                        }
                    }
                }
                Divider()
            }


        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
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