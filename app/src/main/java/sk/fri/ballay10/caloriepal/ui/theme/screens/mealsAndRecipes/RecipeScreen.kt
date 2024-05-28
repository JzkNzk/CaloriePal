package sk.fri.ballay10.caloriepal.ui.theme.screens.mealsAndRecipes

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import sk.fri.ballay10.caloriepal.R
import sk.fri.ballay10.caloriepal.data.Meal
import sk.fri.ballay10.caloriepal.data.Recipe
import sk.fri.ballay10.caloriepal.ui.theme.TopDescriptionBar
import sk.fri.ballay10.caloriepal.ui.theme.colorBlue1
import sk.fri.ballay10.caloriepal.ui.theme.colorBlue2
import sk.fri.ballay10.caloriepal.ui.theme.colorRed1
import sk.fri.ballay10.caloriepal.ui.theme.colorRed2
import sk.fri.ballay10.caloriepal.ui.theme.screens.AppViewModelProvider
import sk.fri.ballay10.caloriepal.viewModels.MealsAndRecipesViewModel
import sk.fri.ballay10.caloriepal.viewModels.SummaryDetails
import sk.fri.ballay10.caloriepal.viewModels.calculateByWeight

@Composable
fun RecipeScreen(viewModel: MealsAndRecipesViewModel = viewModel(factory = AppViewModelProvider.Factory), moveToAddingScreen: (Int) -> Unit, summaryDetails: SummaryDetails, onMealToSummary: (SummaryDetails) -> Unit) {
    Scaffold(
        topBar = {
            TopDescriptionBar("MEALS & RECIPES")
        },
        floatingActionButton = {
            AddItemInTabButton(
            onClick = {
                moveToAddingScreen(viewModel.pageUiState.currentPage)
            },
            color = viewModel.pageUiState.color)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)

        ) {
            ChoosableTabs(viewModel, onPageChange = {
                if (it == 0) {
                    viewModel.updatePageUiState(0)
                }
                if (it == 1) {
                    viewModel.updatePageUiState(1)
                }
            }, onMealToSummary =  {meal ->
                onMealToSummary(summaryDetails.copy(
                    consumedFats = meal.totalFats.toString(),
                    consumedCarbs = meal.totalCarbs.toString(),
                    consumedProteins = meal.totalProtein.toString(),
                    consumedCalories = meal.totalCalories.toString()))
            })
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChoosableTabs(viewModel: MealsAndRecipesViewModel, onPageChange: (Int) -> Unit, onMealToSummary: (Meal) -> Unit) {
    val recipeList by viewModel.recipeListUiState.collectAsState()
    val mealList by viewModel.mealListUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Column {
        val pagerState = rememberPagerState(pageCount = {2})

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = Color(130,130,130)
        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                text = { Text(text = "Meals", color = Color(255,255,255))},
                onClick = {
                    coroutineScope.launch {
                        onPageChange(0)
                        pagerState.animateScrollToPage(0)
                    }
                }
            )
            Tab(
                selected = pagerState.currentPage == 1,
                text = { Text(text = "Recipes", color = Color(255,255,255))},
                onClick = {
                    coroutineScope.launch {
                        onPageChange(1)
                        pagerState.animateScrollToPage(1)
                    }
                }
            )
        }
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> {
                    MealsPage(
                        mealList.mealList,
                        onMealToSummary = { addedMeal ->
                            onMealToSummary(addedMeal)
                        },
                        onRemoveMeal = {
                            coroutineScope.launch {
                                viewModel.deleteMeal(it)
                            }
                        }
                    )
                }
                1 -> {
                    RecipesPage(
                        recipeList.recipeList,
                        onRemoveRecipe = {
                            coroutineScope.launch {
                                viewModel.deleteRecipe(it)
                            }
                        }
                    )
                }
            }
        }
    }
}

// Displays the list of meals from database
@Composable
fun MealsPage(listOfMeals: List<Meal>, onMealToSummary: (Meal) -> Unit, onRemoveMeal: (Meal) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {items(listOfMeals){
            MealTabItem(meal = it, onRemoveItem = { temp ->
                onRemoveMeal(temp)
            }, onMealToSummary = { addedMeal ->
                onMealToSummary(addedMeal)})
        }
    }
}

// Displays the list of recipes from database
@Composable
fun RecipesPage(listOfRecipes: List<Recipe>, onRemoveRecipe: (Recipe) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(listOfRecipes){
            RecipeTabItem(recipe = it, onRemoveItem = { temp ->
                onRemoveRecipe(temp)
            }
            )
        }
    }
}

@Composable
fun RecipeTabItem(recipe: Recipe, onRemoveItem: (Recipe) -> Unit) {
    var isRecipeWindowVisible by rememberSaveable {
        mutableStateOf(false)
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable {
            isRecipeWindowVisible = true
        }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .size(92.dp)
                .background(colorRed1)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(80.dp)
                    .background(colorRed2)
            ) {
                Text(text = "Recipe", style = MaterialTheme.typography.body2)
            }

            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = recipe.name, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold)
                Text(text = "Ingredient count: ${recipe.ingredientCount}")
                Text(text = "Calories: ${recipe.totalCalories} kcal")
                Text(text = "Protein: ${recipe.totalProtein} g, Fats: ${recipe.totalFats} g, Carbs: ${recipe.totalCarbs} g")
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center

            ) {
                IconButton(onClick = {onRemoveItem(recipe)}) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
        }
    }
    if (isRecipeWindowVisible) {
        RecipeShowWindow(recipe = recipe, onExit = {
            isRecipeWindowVisible = false
        })
    }
}

@Composable
fun MealTabItem(meal: Meal, onRemoveItem: (Meal) -> Unit, onMealToSummary: (Meal) -> Unit) {
    var isRecipeWindowVisible by rememberSaveable {
        mutableStateOf(false)
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { isRecipeWindowVisible = true }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .size(92.dp)
                .background(colorBlue1)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(80.dp)
                    .background(colorBlue2)
            ) {
                Text(text = "Meal", style = MaterialTheme.typography.body2)
                IconButton(onClick = {onMealToSummary(meal) }) {
                    Icon(Icons.Filled.Done, contentDescription = "Done")
                }
            }

            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = meal.name, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold)
                Text(text = "Recipe count: ${meal.recipeCount}")
                Text(text = "Calories: ${meal.totalCalories} kcal")
                Text(text = "Protein: ${meal.totalProtein} g, Fats: ${meal.totalFats} g, Carbs: ${meal.totalCarbs} g")
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = {onRemoveItem(meal)}) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete" )
                }
            }
        }
    }
    if (isRecipeWindowVisible) {
        MealShowWindow(meal = meal, onExit = {
            isRecipeWindowVisible = false
        })
    }
}

@Composable
fun AddItemInTabButton(onClick: () -> Unit, color: Color) {

    FloatingActionButton(
        onClick = { onClick() },
        backgroundColor = color,
    ) {
        Icon(Icons.Filled.Add, "Floating action button.")
    }
}

@Composable
fun RecipeShowWindow(recipe: Recipe, onExit: () -> Unit) {

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onExit,
        title = {
            Text(text = "Recipe Details")
        },
        text = {
            Column {
                Row (
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Ingredient name")
                    Text(text = "Weight")
                }
                LazyColumn(
                    modifier = Modifier.height(300.dp)
                ) {
                    items(recipe.ingredients) {ingredient ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ingredient.ingredient?.let { painterResource(id = it.imageRes) }
                                ?.let { Image(painter = it, contentDescription = "Ingredient", modifier = Modifier.size(48.dp),
                                    contentScale = ContentScale.Crop) }
                            Spacer(modifier = Modifier.width(8.dp))
                            ingredient.ingredient?.let { Text(text = it.name) }
                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "${ingredient.weight} g")
                                Text(text = "${calculateByWeight(ingredient.ingredient?.calories ?: 0, ingredient.weight)} calories", fontSize = 11.sp)
                            }
                        }
                        HorizontalDivider()
                    }
                }
                Divider()
            }
        },
        confirmButton = {
        },
        dismissButton = {
            TextButton(onClick = onExit) {
                Text("Cancel")
            }
        },
    )
}



@Composable
fun MealShowWindow(meal: Meal, onExit: () -> Unit) {

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onExit,
        title = {
            Text(text = "Recipe Details")
        },
        text = {
            Column {
                Row (
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Recipe name")
                    Text(text = "Calories")
                }

                LazyColumn(
                    modifier = Modifier.height(300.dp)
                ) {
                    items(meal.recipes) {recipe ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(painter = painterResource(id = R.drawable.baseline_sticky_note_2_24), contentDescription = "Recipe" )
                            Text(text = recipe.name)
                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "${recipe.totalCalories} kcal")
                            }
                        }
                        HorizontalDivider()
                    }
                }
                HorizontalDivider()
            }
        },
        confirmButton = {
        },
        dismissButton = {
            TextButton(onClick = onExit) {
                Text("Cancel")
            }
        },
        )
}


