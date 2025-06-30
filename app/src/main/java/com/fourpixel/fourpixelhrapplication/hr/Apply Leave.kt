package com.fourpixel.fourpixelhrapplication.hr


import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fourpixel.fourpixelhrapplication.client.ApiService
import com.fourpixel.fourpixelhrapplication.ui.theme.poppinsFontFamily
import kotlinx.coroutines.launch
import java.util.*
import com.fourpixel.fourpixelhrapplication.client.RetrofitClient


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyLeavesScreen(
    token: String,
    userId: Int, // You'll need to pass userId from where you navigate to this screen
    navController: NavController,
    viewModel: ApplyLeavesViewModel = viewModel(factory = ApplyLeaveViewModelFactory(apiService =RetrofitClient.instance.create(ApiService::class.java)))
) {
    val context = LocalContext.current

    // Observe ViewModel states directly
    val leaveDate = viewModel.leaveDate
    val reason = viewModel.reason
    val selectedLeaveType = viewModel.selectedLeaveType
    val selectedDuration = viewModel.selectedDuration
    val isLoading = viewModel.isLoading

    // Collect events from the ViewModel
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.applyLeaveEvent.collect { event ->
            when (event) {
                is ApplyLeaveEvent.Success -> {
                    scope.launch { snackbarHostState.showSnackbar(event.message) }
                    navController.popBackStack() // Navigate back on success
                }
                is ApplyLeaveEvent.Error -> {
                    scope.launch { snackbarHostState.showSnackbar(event.message) }
                }
            }
        }
    }


    // Setup date picker
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            viewModel.onLeaveDateSelected(selectedDay, selectedMonth, selectedYear)
        },
        year, month, day
    )

    // Leave types to choose from (can be moved to ViewModel if dynamic)
    val leaveTypes = listOf("Annual Leave", "Casual Leave", "Medical Leave")
    var expandedLeaveType by remember { mutableStateOf(false) } // This can remain local UI state
    var leaveTypeTextFieldWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    // Duration types to choose from (can be moved to ViewModel if dynamic)
    val durationTypes = listOf("Full Day", "First Half", "Second Half")
    var expandedDuration by remember { mutableStateOf(false) } // This can remain local UI state
    var durationTextFieldWidth by remember { mutableStateOf(0.dp) }


    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues -> // Add Scaffold for Snackbar
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(20.dp)
                .padding(paddingValues) // Apply padding from Scaffold
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            // Top Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                }
                Text(
                    text = "Apply Leave",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily
                )

            }

            // Leave Type Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedLeaveType,
                onExpandedChange = { expandedLeaveType = !expandedLeaveType },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        // Set the width of the dropdown menu to match the TextField
                        leaveTypeTextFieldWidth = with(density) { coordinates.size.width.toDp() }
                    }
            ) {
                TextField(
                    value = selectedLeaveType, // Use ViewModel's state
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Leave Type", color = Color.Gray, fontFamily = poppinsFontFamily ) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLeaveType) },
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
                    expanded = expandedLeaveType,
                    onDismissRequest = { expandedLeaveType = false },
                    modifier = Modifier.width(leaveTypeTextFieldWidth)
                ) {
                    leaveTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                viewModel.onLeaveTypeSelected(type) // Call ViewModel function
                                expandedLeaveType = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Duration Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedDuration,
                onExpandedChange = { expandedDuration = !expandedDuration },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        durationTextFieldWidth = with(density) { coordinates.size.width.toDp() }
                    }
            ) {
                TextField(
                    value = selectedDuration, // Use ViewModel's state
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Duration", color = Color.Gray, fontFamily = poppinsFontFamily) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDuration) },
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
                    expanded = expandedDuration,
                    onDismissRequest = { expandedDuration = false },
                    modifier = Modifier.width(durationTextFieldWidth) // Use durationTextFieldWidth
                ) {
                    durationTypes.forEach { durationType ->
                        DropdownMenuItem(
                            text = { Text(durationType) },
                            onClick = {
                                viewModel.onDurationSelected(durationType) // Call ViewModel function
                                expandedDuration = false
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
                        text = leaveDate, // Use ViewModel's state
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
                value = reason, // Use ViewModel's state
                onValueChange = { viewModel.onReasonChanged(it) }, // Call ViewModel function
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
                onClick = { viewModel.applyLeave(token, userId) }, // Call ViewModel function
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4B740)),
                enabled = !isLoading // Disable button while loading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
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
    }
}

// You'll need a ViewModelFactory to provide the ApiService to your ViewModel
class ApplyLeaveViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ApplyLeavesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ApplyLeavesViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



