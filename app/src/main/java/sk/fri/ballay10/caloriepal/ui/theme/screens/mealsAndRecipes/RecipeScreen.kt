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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import sk.fri.ballay10.caloriepal.data.ChoosenIngredient
import sk.fri.ballay10.caloriepal.data.Ingredient
import sk.fri.ballay10.caloriepal.objects.IngredietList
import sk.fri.ballay10.caloriepal.data.Meal
import sk.fri.ballay10.caloriepal.data.Recipe
import sk.fri.ballay10.caloriepal.ui.theme.TopDescriptionBar
import sk.fri.ballay10.caloriepal.ui.theme.colorBlue1
import sk.fri.ballay10.caloriepal.ui.theme.colorBlue2
import sk.fri.ballay10.caloriepal.ui.theme.colorRed1
import sk.fri.ballay10.caloriepal.ui.theme.colorRed2
import sk.fri.ballay10.caloriepal.ui.theme.screens.AppViewModelProvider
import sk.fri.ballay10.caloriepal.viewModels.MealsAndRecipesViewModel

@Composable
fun RecipeScreen(viewModel: MealsAndRecipesViewModel = viewModel(factory = AppViewModelProvider.Factory), moveToAddingScreen: (Int) -> Unit) {
    var btnColor by remember {
        mutableStateOf(colorBlue1)
    }
    // Tracks which tab is active
    var activeTab by rememberSaveable {
        mutableIntStateOf(0)
    }
    //Tracks if user is creating new entry
    var addingState by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopDescriptionBar("MEALS & RECIPES")
        },
        floatingActionButton = {
            AddItemInTabButton(
            onClick = {
                moveToAddingScreen(activeTab)
            },
            color = btnColor)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)

        ) {
            ChoosableTabs(viewModel, onPageChange = {
                if (it == 0) {
                    btnColor = colorBlue1
                    activeTab = 0
                }
                if (it == 1) {
                    btnColor = colorRed1
                    activeTab = 1
                }
            }, onMealToSummary =  {
                // Adds meal to current summary
            })
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChoosableTabs(viewModel: MealsAndRecipesViewModel, onPageChange: (Int) -> Unit, onMealToSummary: (Meal) -> Unit) {
    Column {
        val pagerState = rememberPagerState(pageCount = {2})
        val corutineScope = rememberCoroutineScope()
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = Color(130,130,130)
        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                text = { Text(text = "Meals", color = Color(255,255,255))},
                onClick = {
                    corutineScope.launch {
                        onPageChange(0)
                        pagerState.animateScrollToPage(0)
                    }
                }
            )
            Tab(
                selected = pagerState.currentPage == 1,
                text = { Text(text = "Recipes", color = Color(255,255,255))},
                onClick = {
                    corutineScope.launch {
                        onPageChange(1)
                        pagerState.animateScrollToPage(1)
                    }
                }
            )
        }
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> {
                    MealsPage(viewModel, onMealToSummary = { addedMeal ->
                        onMealToSummary(addedMeal)
                    })

                }
                1 -> {
                    RecipesPage(viewModel)
                }
            }
        }
    }
}

// Displays the list of meals from database
@Composable
fun MealsPage(viewModel: MealsAndRecipesViewModel, onMealToSummary: (Meal) -> Unit) {
//    val mealList by viewModel.mealList.observeAsState()
//    mealList?.let {
//        LazyColumn {items(it){
//                MealTabItem(meal = it, onRemoveItem = { temp ->
//                    viewModel.removeMealFromList(temp)
//                }, onMealToSummary = { addedMeal ->
//                    onMealToSummary(addedMeal)})
//            }
//        }
//    }
}

// Displays the list of recipes from database
@Composable
fun RecipesPage(viewModel: MealsAndRecipesViewModel) {
//    val recipeList by viewModel.recipeList.observeAsState()
//    recipeList?.let {
//        LazyColumn {
//            items(it){
//                RecipeTabItem(recipe = it, onRemoveItem = { temp ->
//                    viewModel.removeRecipeFromList(temp)
//                }
//                )
//            }
//        }
//    }
}

@Composable
fun RecipeTabItem(recipe: Recipe, onRemoveItem: (Int) -> Unit) {
    val savedRecipe by remember {
        mutableStateOf(recipe)
    }
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
                IconButton(onClick = {onRemoveItem(recipe.id)}) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
        }
    }
    if (isRecipeWindowVisible) {
        RecipeShowWindow(recipe = savedRecipe, onExit = {
            isRecipeWindowVisible = false
        })
    }
}

@Composable
fun MealTabItem(meal: Meal, onRemoveItem: (Int) -> Unit, onMealToSummary: (Meal) -> Unit) {
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
                IconButton(onClick = {onRemoveItem(meal.id)}) {
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
fun IngredientChoosingScreen(ingredients: List<Ingredient>, onClose: () -> Unit, onAddIngredient: (ChoosenIngredient) -> Unit) {
    val scrollState = rememberScrollState()
    var weightField by rememberSaveable {
        mutableStateOf("")
    }
    var selectedOption by rememberSaveable { mutableStateOf<Ingredient?>(null) }

    Dialog(
        onDismissRequest = {} ,
        DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .zIndex(10f)
                .verticalScroll(scrollState)
        ) {
            Column (
                modifier = Modifier
                    .background(Color.DarkGray)
                    .width(200.dp)
                    .padding(8.dp)

            ) {
                Text("Choose Ingredient", color = Color.White)
            }
            LazyColumn(
                modifier = Modifier
                    .background(Color.Gray)
                    .width(200.dp)
                    .height(400.dp),

                ) {items(ingredients) { item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = (item == selectedOption),
                        onClick = { selectedOption = item }
                    )
                    Text(
                        text = item.name,
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            }
            Column (
                modifier = Modifier
                    .background(Color.DarkGray)
                    .width(200.dp)
                    .padding(8.dp)

            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text("Weight (g): ", color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    BasicTextField(value = weightField,
                        onValueChange = { value ->
                            weightField = value
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .height(30.dp)
                            .background(Color(50, 50, 50))
                            .padding(5.dp),
                        textStyle = LocalTextStyle.current.copy(color = Color.White),

                        )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(onClick = { onClose()}) {
                        Text(text = "Cancel", color = Color.White)
                    }
                    Button(onClick = {
                        if(selectedOption != null && weightField != "") {
                            selectedOption?.let { ChoosenIngredient(it, weightField.toInt()) }
                                ?.let { onAddIngredient(it) }
                        }
                    }) {
                        Text(text = "Add", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun BasicListEntry(ingredient: Ingredient, weight: Int, onRemoveItem: () -> Unit, removeable: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color.White)
            .padding(8.dp)) {
        Image(
            painter = painterResource(id = ingredient.imageRes),
            contentDescription = ingredient.name,
            modifier = Modifier.size(48.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = ingredient.name)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "Grams: $weight g")
        if (removeable) {
            IconButton(onClick = {onRemoveItem()}) {
                Icon(Icons.Default.Close, contentDescription = "Remove")
            }
        }
    }
    Divider()
}

@Composable
fun RecipeListEntry(recipe: Recipe, amount: Int, onRemoveItem: () -> Unit, removeable: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color.White)
            .padding(8.dp)) {
        Text(text = recipe.name)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "Amount: $amount")
        if (removeable) {
            IconButton(onClick = {onRemoveItem()}) {
                Icon(Icons.Default.Close, contentDescription = "Remove")
            }
        }
    }
    Divider()
}

@Composable
fun RecipeShowWindow(recipe: Recipe, onExit: () -> Unit) {
    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = { onExit() }, DialogProperties(
        usePlatformDefaultWidth = false
    )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column (
                modifier = Modifier
                    .padding(36.dp)
                    .background(Color.DarkGray)
                    .verticalScroll(scrollState)

            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorRed1)
                        .width(200.dp)
                        .padding(8.dp)
                ) {
                    Text(text = "Recipe", color = Color.Black, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = recipe.name,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Ingredients:", color = Color.White)
                }
                Divider()
//                LazyColumn(modifier = Modifier
//                    .height(250.dp)
//                    .padding(8.dp)) {
//                    items(recipe.ingredients){item ->
//                        BasicListEntry(ingredient = item.ingredient, weight = item.weight, onRemoveItem = {}, removeable = false)
//                        }
//                    }
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Column (
//                        modifier = Modifier.padding(start = 8.dp)
//                    ) {
//                        Text(text = "Total Calories: ${recipe.totalCalories} kcal", color = Color.White)
//                        Text(text = "Protein: ${recipe.totalProtein} g, Carbs: ${recipe.totalCarbs} g, Fats: ${recipe.totalFats} g", color = Color.White)
//                    }
//                    Row(modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp, 0.dp, 8.dp), horizontalArrangement = Arrangement.Center) {
//                        Button(onClick = {onExit()  }) {
//                            Text("Exit", color = Color.White)
//                        }
//                    }
//                }
//
//            }
            }
            }
        }
}

@Composable
fun MealShowWindow(meal: Meal, onExit: () -> Unit) {
    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = { onExit() }, DialogProperties(
        usePlatformDefaultWidth = false
    )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Column (
                modifier = Modifier
                    .padding(36.dp)
                    .background(Color.DarkGray)

            ) {
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .background(colorBlue1)
                    .width(200.dp)
                    .padding(8.dp)
                ) {
                    Text(text = "Meal", color = Color.Black, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = meal.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Recipes:", color = Color.White)
                }
                Divider()
                LazyColumn(modifier = Modifier
                    .height(150.dp)
                    .padding(8.dp)) {
//                    items(meal.meals){item ->
//                        RecipeListEntry(recipe = item.recipe, amount = item.amount, onRemoveItem = {}, removeable = false)
//                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column (
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(text = "Total Calories: ${meal.totalCalories} kcal", color = Color.White)
                    Text(text = "Protein: ${meal.totalProtein} g, Carbs: ${meal.totalCarbs} g, Fats: ${meal.totalFats} g", color = Color.White)
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 0.dp, 8.dp), horizontalArrangement = Arrangement.Center) {
                    Button(onClick = {onExit()  }) {
                        Text("Exit", color = Color.White)
                    }
                }
            }

        }
    }
}


