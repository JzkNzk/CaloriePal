package sk.fri.ballay10.caloriepal

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import sk.fri.ballay10.caloriepal.objects.IngredietList
import sk.fri.ballay10.caloriepal.roomdb.MealDatabase
import sk.fri.ballay10.caloriepal.roomdb.NutrientSummaryDatabase
import sk.fri.ballay10.caloriepal.roomdb.RecipeDatabase
import sk.fri.ballay10.caloriepal.ui.theme.screens.NavigationManager
import sk.fri.ballay10.caloriepal.viewModels.MealsAndRecipesViewModel
import sk.fri.ballay10.caloriepal.viewModels.SummaryPageViewModel


class MainActivity : ComponentActivity() {
    companion object {
        lateinit var recipeDatabase: RecipeDatabase
        lateinit var mealDatabase: MealDatabase
        lateinit var nutrientSummaryDatabase: NutrientSummaryDatabase
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IngredietList.initializeIngredientListContext(this)
        //Databases
        recipeDatabase = Room.databaseBuilder(
            applicationContext,
            RecipeDatabase::class.java,
            RecipeDatabase.NAME
        ).build()

        mealDatabase = Room.databaseBuilder(
            applicationContext,
            MealDatabase::class.java,
            MealDatabase.NAME
        ).build()

        nutrientSummaryDatabase = Room.databaseBuilder(
            applicationContext,
            NutrientSummaryDatabase::class.java,
            NutrientSummaryDatabase.NAME
        ).build()

        //ViewModels
        val mealsAndRecipesViewModel = ViewModelProvider(this)[MealsAndRecipesViewModel::class.java]
        val summaryPageViewModel = ViewModelProvider(this)[SummaryPageViewModel::class.java]
        setContent {
            CaloriePalApp(mealsAndRecipesViewModel, summaryPageViewModel)
        }
    }
}

@Composable
fun CaloriePalApp(mealsAndRecipesViewModel: MealsAndRecipesViewModel, summaryPageViewModel: SummaryPageViewModel) {
    NavigationManager(mealsAndRecipesViewModel, summaryPageViewModel)
}

