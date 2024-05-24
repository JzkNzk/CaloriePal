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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigamole.composeshadowsplus.common.shadowsPlus
import sk.fri.ballay10.caloriepal.R
import sk.fri.ballay10.caloriepal.ui.theme.TopDescriptionBar
import sk.fri.ballay10.caloriepal.ui.theme.colorGreen1
import sk.fri.ballay10.caloriepal.ui.theme.colorRed1

@Composable
fun RecipeAddingScreen(modifier: Modifier = Modifier) {
    Scaffold (
        topBar = { TopDescriptionBar(title = "Add recipe") }

    ){
        Column (
            modifier
                .padding(paddingValues = it)
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
        ) {
            EnterNameForm()
            Spacer(modifier = modifier.height(16.dp))
            PickedItemList()
            ActionButtons()
        }
    }
}

@Preview
@Composable
fun RecipeAddingScreenPreview(modifier: Modifier = Modifier) {
    RecipeAddingScreen()
}

@Composable
fun EnterNameForm(modifier: Modifier = Modifier) {
    Column (
        modifier = modifier
            .shadowsPlus()
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(15.dp))
            .padding(8.dp)
    ) {
        Text(text = "Enter recipe name: ", fontSize = 18.sp)
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(text = "Recipe name")},
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PickedItemList(modifier: Modifier = Modifier) {
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
                IngredientEntry()
            }
            item {
                IngredientEntry()
            }
            item {
                IngredientEntry()
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
            Button(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    }
}

@Composable
fun IngredientEntry() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
            ) {
            Image(
                painter = painterResource(R.drawable.apple),
                contentDescription = "Apple",
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Apple", style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Weight: 100g")
            IconButton(onClick = {}) {
                Icon(Icons.Default.Close, contentDescription = "Remove")
            }
        }
        Divider()
    }
}

@Composable
fun ActionButtons() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(onClick = { /*TODO*/ }, contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp, start = 40.dp, end = 40.dp), colors = ButtonDefaults.buttonColors(containerColor = colorRed1)) {
            Icon(Icons.Filled.Close, contentDescription = "Cancel")
        }
        Button(onClick = { /*TODO*/ }, contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp, start = 40.dp, end = 40.dp), colors = ButtonDefaults.buttonColors(containerColor = colorGreen1)) {
            Icon(Icons.Filled.Done, contentDescription = "Done" )
        }
    }
}

@Composable
fun PickIngredientDialog() {
    var selectedOption by rememberSaveable { mutableStateOf("") }
    androidx.compose.material3.AlertDialog(
        onDismissRequest = { /*TODO*/ },
        title = {
            Text(text = "Pick a ingredient")
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
                            RadioButton(
                                selected = (true),
                                onClick = {

                                }
                            )
                        }
                    }
                }
                Divider()
                androidx.compose.material3.OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Enter weight") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            }


        },
        confirmButton = {
            TextButton(onClick = {}) {
                Text("Pick")
            }
        },
        dismissButton = {
            TextButton(onClick = { }) {
                Text("Cancel")
            }
        },

        )
}

@Preview(showBackground = true)
@Composable
fun PickIngredientDialogPreview() {
    PickIngredientDialog()
}