package sk.fri.ballay10.caloriepal.ui.theme.screens.mealsAndRecipes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gigamole.composeshadowsplus.common.shadowsPlus
import kotlinx.coroutines.launch
import sk.fri.ballay10.caloriepal.R
import sk.fri.ballay10.caloriepal.data.ChoosenIngredient
import sk.fri.ballay10.caloriepal.data.Ingredient
import sk.fri.ballay10.caloriepal.ui.theme.ActionButtons
import sk.fri.ballay10.caloriepal.ui.theme.EnterNameForm
import sk.fri.ballay10.caloriepal.ui.theme.TopDescriptionBar
import sk.fri.ballay10.caloriepal.ui.theme.colorRed1
import sk.fri.ballay10.caloriepal.ui.theme.screens.AppViewModelProvider
import sk.fri.ballay10.caloriepal.viewModels.ChoosenIngredientDetails
import sk.fri.ballay10.caloriepal.viewModels.ChoosingIngredientUiState
import sk.fri.ballay10.caloriepal.viewModels.RecipeAddingViewModel
import sk.fri.ballay10.caloriepal.viewModels.RecipeUiState

@Composable
fun RecipeAddingScreen(viewModel: RecipeAddingViewModel = viewModel(factory = AppViewModelProvider.Factory), modifier: Modifier = Modifier, returnBack:() -> Unit) {
    var isPickDialogVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()
    val verticalScroll = rememberScrollState()

    Scaffold (
        topBar = { TopDescriptionBar(title = "Add recipe", bgColor = colorRed1) },
        bottomBar = {}

    ){
        Column (
            modifier
                .verticalScroll(verticalScroll)
                .padding(paddingValues = it)
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
        ) {
            EnterNameForm(nameOfWhat = stringResource(id = R.string.recipe_lowercase),
                onNameChange = {name ->
                    viewModel.updateRecipeUiState(viewModel.recipeUiState.createdRecipe.copy(name = name))
                },
                value = viewModel.recipeUiState.createdRecipe.name
            )
            Spacer(modifier = modifier.height(16.dp))
            PickedIngredientList(
                onAddIngredient = {
                    isPickDialogVisible = true
                },
                removeIngredient = {ingredient ->
                    viewModel.removeChosenIngredientFromRecipe(ingredient)
                },
                recipeUiState = viewModel.recipeUiState,
            )
            ActionButtons(onCancel = returnBack, onConfirm = {
                coroutineScope.launch {
                    viewModel.saveRecipe()
                }
                returnBack()
            })
        }
        if (isPickDialogVisible) {
            PickIngredientDialog(
                onCancel = {isPickDialogVisible = false},
                ingredients = viewModel.ingredientList,
                choosingIngredientUiState = viewModel.choosingIngredientUiState,
                onValueChange = viewModel::updateChoosingUiState,
                onIngredientChange = viewModel::updateChoosingUiState,
                onConfirm = {
                    viewModel.addChosenIngredientToRecipe()
                    isPickDialogVisible = false
                }
            )
        }
    }
}

@Composable
fun PickedIngredientList(
    onAddIngredient: () -> Unit,
    modifier: Modifier = Modifier,
    recipeUiState: RecipeUiState,
    removeIngredient: (ChoosenIngredient) -> Unit) {
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
        HorizontalDivider()
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            recipeUiState.createdRecipe.ingredients.let {
                items(it.toList()) { item ->
                    IngredientEntry(item, onRemove = {
                        removeIngredient(item)
                    })
                }
            }
        }
        HorizontalDivider()
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Column {
                Row {
                    Text(text = "Total calories: ", fontWeight = FontWeight.Bold)
                    Text(text = recipeUiState.createdRecipe.totalCalories)
                }
                Row {
                    Text(text = "Protein: ", fontWeight = FontWeight.Bold)
                    Text(text = "${recipeUiState.createdRecipe.totalProtein}g, ")

                    Text(text = "Fats: ", fontWeight = FontWeight.Bold)
                    Text(text = "${recipeUiState.createdRecipe.totalFats}g, ")

                    Text(text = "Carbs: ", fontWeight = FontWeight.Bold)
                    Text(text = "${recipeUiState.createdRecipe.totalCarbs}g ")
                }
            }
            Button(onClick = onAddIngredient) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    }
}

@Composable
fun IngredientEntry(choosenIngredient: ChoosenIngredient, onRemove: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
            ) {
            choosenIngredient.ingredient?.let { painterResource(it.imageRes) }?.let {
                Image(
                    painter = it,
                    contentDescription = "Apple",
                    modifier = Modifier.size(48.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            choosenIngredient.ingredient?.let { Text(text = it.name) }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "${choosenIngredient.weight} g")
            IconButton(onClick = {onRemove()}) {
                Icon(Icons.Default.Close, contentDescription = "Remove")
            }
        }
        HorizontalDivider()
    }
}

@Composable
fun PickIngredientDialog(
    onCancel: () -> Unit,
    choosingIngredientUiState: ChoosingIngredientUiState,
    ingredients: List<Ingredient>,
    onValueChange: (ChoosenIngredientDetails) -> Unit,
    onIngredientChange: (ChoosenIngredientDetails) -> Unit,
    onConfirm: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(text = "Pick a ingredient")
        },
        text = {
            Column {
                LazyColumn(
                    modifier = Modifier.height(300.dp)
                ) {
                    items(ingredients) {ingredient ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                }
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = ingredient.name)
                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "${ingredient.calories} kcal")
                                Text(text = "per 100 g", fontSize = 11.sp)
                            }

                            RadioButton(
                                selected = ingredient.name == (choosingIngredientUiState.chosenIngredientDetails.ingredient?.name
                                    ?: false),
                                onClick = {
                                    onIngredientChange(choosingIngredientUiState.chosenIngredientDetails.copy(ingredient = ingredient))
                                }
                            )
                        }
                    }
                }
                HorizontalDivider()
                Text(text = "Weight")
                androidx.compose.material3.OutlinedTextField(
                    value = choosingIngredientUiState.chosenIngredientDetails.weight,
                    onValueChange = {onValueChange(choosingIngredientUiState.chosenIngredientDetails.copy(weight = it))},
                    label = { Text("Enter weight") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
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
