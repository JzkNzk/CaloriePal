package sk.fri.ballay10.caloriepal.ui.theme.screens.summary

import android.icu.util.Calendar
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigamole.composeshadowsplus.common.shadowsPlus
import sk.fri.ballay10.caloriepal.objects.CalendarProvider
import sk.fri.ballay10.caloriepal.ui.theme.TopDescriptionBar
import sk.fri.ballay10.caloriepal.viewModels.SummaryPageViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import sk.fri.ballay10.caloriepal.data.NutrientSummary
import sk.fri.ballay10.caloriepal.ui.theme.screens.AppViewModelProvider
import sk.fri.ballay10.caloriepal.viewModels.SummaryDetails
import sk.fri.ballay10.caloriepal.viewModels.SummaryUiState


@Composable
fun CalorieScreen(viewModel: SummaryPageViewModel) {
    val scrollState = rememberScrollState()
    val corutineScope = rememberCoroutineScope()
    var isDialogActive by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopDescriptionBar(title = "SUMMARY")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            //START OF DATE PICKING
            HorizontalDivider()
            DateRow(
                summaryUiState = viewModel.summaryUiState,
                onSelectedDate = {
                    corutineScope.launch { viewModel.updateSummaryUiStateDay(it) }
                }
            )
            HorizontalDivider()
            NutrientSection(viewModel.summaryUiState.displayedSummary, onGoalSet = {
                isDialogActive = true
            })
        }
        if (isDialogActive) {
            SetDialog(
                summaryDetails = viewModel.summaryUiState.displayedSummary,
                onSetGoal = {item ->
                    isDialogActive = false
                    corutineScope.launch { viewModel.updateSummaryUiStateStatistics(item) }
                },
                onCancel = {
                    isDialogActive = false
                }
            )
        }
    }
}

@Composable
fun DateRow(
    summaryUiState: SummaryUiState,
    onSelectedDate: (SummaryUiState) -> Unit,
) {
    val context = LocalContext.current
    var showDialog by rememberSaveable { mutableStateOf(false) }

    // DatePickerDialog listener to get the selected date
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            onSelectedDate(summaryUiState.copy(selectedDay = dayOfMonth.toString(), selectedMonth = month.toString(), selectedYear = year.toString()))
        },
        summaryUiState.selectedYear.toInt(),
        summaryUiState.selectedMonth.toInt(),
        summaryUiState.selectedDay.toInt()
    ).apply {
        // Restrict to past or current date
        datePicker.maxDate = CalendarProvider.calendar.timeInMillis
    }

    // Show the date picker dialog when showDialog is true
    if (showDialog) {
        datePickerDialog.show()
        showDialog = false
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)

    ) {
        Text(text = "${summaryUiState.selectedDay}/${summaryUiState.selectedMonth}/${summaryUiState.selectedYear}", fontSize = 16.sp)
        IconButton(onClick = { showDialog = true }, modifier = Modifier.padding(0.dp)) {
            Icon(painter = painterResource(id = android.R.drawable.ic_menu_my_calendar), contentDescription = "Calendar")
        }
    }
}

@Composable
fun NutrientSection(displayedSummary: SummaryDetails, onGoalSet: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(125.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NutrientTabBig(description = "Consumed", measure = "kcal", value = displayedSummary.consumedCalories.toInt(), false) {}
            NutrientTabBig(description = "Goal", measure = "kcal", value = displayedSummary.setCalories.toInt(), true, onValueSet = {
                onGoalSet()
            })
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
        Row {
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Nutrient tracking", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
        Row {
            Spacer(modifier = Modifier.width(16.dp))
            NutrientTabSmallTrackable(macronutrient = "Calories", amount = displayedSummary.consumedCalories.toInt(), trackedAmount = displayedSummary.setCalories.toInt(), bgColor = Color.White)
        }
        Row {
            Spacer(modifier = Modifier.width(16.dp))
            NutrientTabSmall(
                macronutrient = "Proteins", amount = displayedSummary.consumedProteins.toInt(), bgColor = Color(194, 160, 160, 255)
            )
        }
        Row {
            Spacer(modifier = Modifier.width(16.dp))
            NutrientTabSmall(macronutrient = "Fats", amount = displayedSummary.consumedFats.toInt(), bgColor = Color(
                230,
                211,
                155,
                255
            )
            )
        }
        Row {
            Spacer(modifier = Modifier.width(16.dp))
            NutrientTabSmall(macronutrient = "Carbohydrates", amount = displayedSummary.consumedFats.toInt(), bgColor = Color(
                241,
                129,
                129,
                255
            )
            )
        }
    }
}

@Composable
fun NutrientTabBig(description: String, measure: String, value: Int, showBtn : Boolean, onValueSet: () -> Unit) {

    Column (
        modifier = Modifier
            .defaultMinSize(minWidth = 150.dp)
            .padding(8.dp)
            .fillMaxHeight()
            .shadowsPlus()
            .background(color = Color.White, shape = RoundedCornerShape(15.dp))
            .padding(8.dp)


    ) {
        Text(text = "$description: ", fontSize = 16.sp)
        Text(text = "$value $measure", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        if (showBtn) {
            Button(onClick = { onValueSet() }, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),modifier = Modifier.defaultMinSize(1.dp, 1.dp)) {
                Text(text = "Set Goal")
            }
        }
    }
}

@Composable
fun NutrientTabSmall(macronutrient: String, amount: Int, bgColor: Color) {
    Column (
        modifier = Modifier
            .defaultMinSize(minWidth = 150.dp)
            .padding(8.dp)
            .shadowsPlus()
            .background(color = bgColor, shape = RoundedCornerShape(15.dp))
            .padding(8.dp)


    ) {
        Text(text = "$macronutrient:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Row {
            Text(text = "$amount", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = " grams", fontSize = 20.sp)
        }
    }
}

@Composable
fun NutrientTabSmallTrackable(macronutrient: String, amount: Int, trackedAmount: Int, bgColor: Color) {
    val successColor = Color(165, 233, 165, 255)
    // Color used for background
    var usedColor by remember {
        mutableStateOf(bgColor)
    }

    usedColor = if (amount >= trackedAmount) {
        successColor
    } else {
        bgColor
    }
    Column (
        modifier = Modifier
            .defaultMinSize(minWidth = 150.dp)
            .padding(8.dp)
            .shadowsPlus() // Custom shadow
            .background(color = usedColor, shape = RoundedCornerShape(15.dp))
            .padding(8.dp)


    ) {
        Text(text = "$macronutrient:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Row {
            Text(text = "$amount", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = " / $trackedAmount kcal", fontSize = 20.sp)
        }
    }
}

@Composable
fun SetDialog(summaryDetails: SummaryDetails, onSetGoal: (SummaryDetails) -> Unit, onCancel: () -> Unit) {
    var goal by rememberSaveable { mutableStateOf("") }
    val isValidInput = goal.toIntOrNull() != null

    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(text = "Set Goal")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = goal,
                    onValueChange = { goal = it },
                    label = { Text("Enter goal") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isValidInput) {
                        onSetGoal(summaryDetails.copy(setCalories = goal))
                    }
                },
                enabled = isValidInput
            ) {
                Text("Set")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
        }
    )
}