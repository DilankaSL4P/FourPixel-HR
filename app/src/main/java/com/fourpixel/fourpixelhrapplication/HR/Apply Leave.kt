package com.fourpixel.fourpixelhrapplication.HR

import androidx.compose.ui.tooling.preview.Preview
import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fourpixel.fourpixelhrapplication.ui.theme.poppinsFontFamily
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyLeavesScreen() {
    val context = LocalContext.current


    var leaveDate by remember { mutableStateOf("Leave Date") }
    var reason by remember { mutableStateOf("") }

    // Setup date picker
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // Leave types to choose from
    val leaveTypes = listOf("Annual Leave", "Casual Leave", "Medical Leave")

    var expanded by remember { mutableStateOf(false) } // for dropdown menu
    var selectedLeaveType by remember { mutableStateOf("") }


    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            leaveDate = "${selectedDay}/${selectedMonth + 1}/$selectedYear"
        },
        year, month, day
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            IconButton(onClick = { }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
            }
            Text(
                text = "Apply Leave",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            )

        }

        // Leave Type Dropdown (static for now)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = selectedLeaveType,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Leave Type", color = Color.Gray, fontFamily = poppinsFontFamily ) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .background(Color(0xFFF2F2F2), shape = RoundedCornerShape(12.dp))
                    .fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF2F2F2),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                leaveTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            selectedLeaveType = type
                            expanded = false
                        }
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Leave Date Picker Field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(Color(0xFFF2F2F2), shape = RoundedCornerShape(12.dp))
                .clickable { datePickerDialog.show() }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = leaveDate,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = poppinsFontFamily
                )
                Icon(
                    imageVector = Icons.Outlined.CalendarToday,
                    contentDescription = "Calendar Icon"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reason TextField
        TextField(
            value = reason,
            onValueChange = { reason = it },
            placeholder = { Text("Reason", color = Color.Gray,fontFamily = poppinsFontFamily) },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color(0xFFF2F2F2), shape = RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF2F2F2),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Submit Button
        Button(
            onClick = { /* Handle Submit */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4B740))
        ) {
            Text(
                text = "Submit",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = poppinsFontFamily
            )
        }
    }
}



