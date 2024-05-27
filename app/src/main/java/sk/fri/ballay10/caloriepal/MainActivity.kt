package sk.fri.ballay10.caloriepal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import sk.fri.ballay10.caloriepal.ui.theme.screens.NavigationManager


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            CaloriePalApp()
        }
    }
}

@Composable
fun CaloriePalApp() {
    NavigationManager()
}

