package com.fourpixel.fourpixelhrapplication.Work

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fourpixel.fourpixelhrapplication.ui.theme.poppinsFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewTaskScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add New Task", fontSize = 20.sp, fontWeight = FontWeight.Bold,fontFamily = poppinsFontFamily,)
        }

        Spacer(modifier = Modifier.height(24.dp))

        TaskDropdownField(label = "Project")
        Spacer(modifier = Modifier.height(12.dp))

        TaskTextField(label = "Title")
        Spacer(modifier = Modifier.height(12.dp))

        TaskTextField(label = "Description", height = 120.dp)
        Spacer(modifier = Modifier.height(12.dp))

        TaskDateField(label = "Start Date")
        Spacer(modifier = Modifier.height(12.dp))

        TaskDateField(label = "End Date")
        Spacer(modifier = Modifier.height(12.dp))

        TaskDropdownField(label = "Task Category")
        Spacer(modifier = Modifier.height(12.dp))

        TaskDropdownField(label = "Status")
        Spacer(modifier = Modifier.height(12.dp))

        TaskDropdownField(label = "Priority")
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Submit action */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF0B243)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Submit", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = poppinsFontFamily)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTextField(label: String, height: Dp = 56.dp) {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color(0xFFF5F5F5),
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDropdownField(label: String) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf("") }

    Box {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            placeholder =  {
            Text(label, fontFamily = poppinsFontFamily)
        },
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { expanded = true },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFFF5F5F5),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp)
        )

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Option 1") }, onClick = {
                selected = "Option 1"
                expanded = false
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDateField(label: String) {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = { Text(label) },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = null)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color(0xFFF5F5F5),
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        readOnly = true
    )
}

/*@Preview(showBackground = true)
@Composable
fun AddTaskPreview() {
    AddNewTaskScreen()
}*/