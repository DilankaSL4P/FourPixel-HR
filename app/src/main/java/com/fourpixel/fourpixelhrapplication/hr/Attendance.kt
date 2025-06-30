package com.fourpixel.fourpixelhrapplication.hr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.fourpixel.fourpixelhrapplication.ui.theme.poppinsFontFamily // Assuming this is defined

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth

// --- Data Models (could be in a separate 'data' package) ---

/**
 * Represents a single attendance entry for a specific date.
 *
 * @param date The date of the attendance in "YYYY-MM-DD" format.
 * @param status The attendance status (e.g., "Present", "Absent", "Leave", "Holiday").
 */
data class AttendanceEntry(
    val date: String,
    val status: String
)

// --- ViewModel (Simplified for demonstration) ---

/**
 * A simplified DashboardViewModel to simulate fetching attendance data.
 * In a real application, this would fetch data from a repository.
 */
class DashboardViewModel : ViewModel() {

    // MutableStateFlow to hold the list of attendance entries.
    private val _attendanceData = MutableStateFlow<List<AttendanceEntry>>(emptyList())
    val attendanceData: StateFlow<List<AttendanceEntry>> = _attendanceData

    init {
        // Simulate initial data loading for the current month
        getAttendance(YearMonth.now().year, YearMonth.now().monthValue)
    }

    fun getAttendance(year: Int, month: Int) {
        viewModelScope.launch {
            // Simulate API call delay
            kotlinx.coroutines.delay(500)

            // Generate mock data for the specified month
            val mockData = mutableListOf<AttendanceEntry>()
            val daysInMonth = YearMonth.of(year, month).lengthOfMonth()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            for (day in 1..daysInMonth) {
                val date = LocalDate.of(year, month, day)
                val status = when {
                    date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY -> "Holiday" // Weekends as holidays
                    day % 7 == 0 -> "Leave" // Simulate some leaves
                    day % 5 == 0 -> "Absent" // Simulate some absences
                    else -> "Present" // Default to present
                }
                mockData.add(AttendanceEntry(date.format(formatter), status))
            }
            _attendanceData.value = mockData
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyAttendanceScreen(
    navController: NavController,
    viewModel: DashboardViewModel = remember { DashboardViewModel() } // Provide a default or inject
) {
    // State to hold the currently displayed month and year
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }

    // Collect attendance data from the ViewModel as a State
    val attendanceData by viewModel.attendanceData.collectAsState()

    // Trigger data fetching whenever the displayed month changes
    LaunchedEffect(currentYearMonth) {
        viewModel.getAttendance(currentYearMonth.year, currentYearMonth.monthValue)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Monthly Attendance",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Month and Year Navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { currentYearMonth = currentYearMonth.minusMonths(1) }) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous Month")
                }
                Text(
                    text = currentYearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily
                )
                IconButton(onClick = { currentYearMonth = currentYearMonth.plusMonths(1) }) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next Month")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Weekday Headers
            val weekdays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                weekdays.forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFontFamily,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Calendar Grid
            val firstDayOfMonth = currentYearMonth.atDay(1)
            val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // 0 for Sunday, 6 for Saturday
            val daysInMonth = currentYearMonth.lengthOfMonth()

            // Create a list of nullable Integers for the calendar grid. Nulls represent empty cells.
            val calendarDays = mutableListOf<Int?>()
            repeat(firstDayOfWeek) { calendarDays.add(null) } // Add leading empty cells
            for (i in 1..daysInMonth) { calendarDays.add(i) } // Add actual days

            LazyVerticalGrid(
                columns = GridCells.Fixed(7), // 7 columns for days of the week
                modifier = Modifier.fillMaxWidth(),
                userScrollEnabled = false // Disable scrolling for calendar grid
            ) {
                items(calendarDays) { day ->
                    if (day != null) {
                        val date = LocalDate.of(currentYearMonth.year, currentYearMonth.month, day)
                        // Find attendance entry for the current day
                        val attendanceEntry = attendanceData.find {
                            it.date == date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        }
                        DayCell(
                            day = day,
                            status = attendanceEntry?.status,
                            isCurrentDay = date == LocalDate.now(),
                            isWeekend = date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY
                        )
                    } else {
                        Spacer(modifier = Modifier.aspectRatio(1f)) // Empty cell for alignment
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Attendance Summary
            AttendanceSummary(attendanceData)
        }
    }
}

/**
 * Composable for displaying a single day cell in the attendance calendar.
 *
 * @param day The day number (e.g., 1, 2, 3...).
 * @param status The attendance status for this day (e.g., "Present", "Absent", "Leave", "Holiday").
 * @param isCurrentDay True if this cell represents the current date.
 * @param isWeekend True if this day is a Saturday or Sunday.
 */
@Composable
fun DayCell(day: Int, status: String?, isCurrentDay: Boolean, isWeekend: Boolean) {
    val backgroundColor = when (status) {
        "Present" -> Color(0xFFFFE8BB) // Light yellow/orange
        "Absent" -> Color(0xFFF8D7DA) // Light red
        "Leave" -> Color(0xFFD4EDDA)
        "Holiday" -> Color(0xFFE8E8E8) // Light blue
        else -> Color.White // Default for no status or unknown
    }

    val textColor = when (status) {
        "Present" -> Color(0xFF856404) // Dark yellow/orange
        "Absent" -> Color(0xFF721C24) // Dark red
        "Leave" -> Color(0xFF155724)
        "Holiday" -> Color(0xFF666869) // Dark blue
        else -> Color.Gray
    }

    val borderColor = if (isCurrentDay) Color(0xFF3B82F6) else Color.Transparent // Accent for current day
    val borderWidth = if (isCurrentDay) 2.dp else 0.dp

    Column(
        modifier = Modifier
            .aspectRatio(1f) // Makes cells square
            .padding(2.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .background(Color.Transparent, RoundedCornerShape(8.dp)) // Secondary background to show border
            .padding(borderWidth) // Padding for border
            .background(backgroundColor, RoundedCornerShape(8.dp)), // Apply background again
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = day.toString(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = poppinsFontFamily,
            color = textColor
        )
        // You could add an icon here based on status if preferred over text
        /*
        val icon = when (status) {
            "Present" -> Icons.Default.CheckCircle
            "Absent" -> Icons.Default.Close
            "Leave" -> Icons.Default.EventBusy
            else -> null
        }
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = status, tint = textColor, modifier = Modifier.size(16.dp))
        }
        */
    }
}

/**
 * Composable to display a summary of attendance.
 *
 * @param attendanceData The list of attendance entries for the month.
 */
@Composable
fun AttendanceSummary(attendanceData: List<AttendanceEntry>) {
    val totalDays = attendanceData.size
    val presentDays = attendanceData.count { it.status == "Present" }
    val absentDays = attendanceData.count { it.status == "Absent" }
    val leaveDays = attendanceData.count { it.status == "Leave" }
    val holidayDays = attendanceData.count { it.status == "Holiday" }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF2F2F2), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Summary",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            fontFamily = poppinsFontFamily,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))

        AttendanceSummaryRow(label = "Total Days:", value = totalDays.toString())
        AttendanceSummaryRow(label = "Present:", value = presentDays.toString(), color = Color(0xFF155724))
        AttendanceSummaryRow(label = "Absent:", value = absentDays.toString(), color = Color(0xFF721C24))
        AttendanceSummaryRow(label = "Leave:", value = leaveDays.toString(), color = Color(0xFF856404))
        AttendanceSummaryRow(label = "Holidays:", value = holidayDays.toString(), color = Color(0xFF0C5460))
    }
}

/**
 * Helper Composable for a single row in the attendance summary.
 */
@Composable
fun AttendanceSummaryRow(label: String, value: String, color: Color = Color.DarkGray) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontFamily = poppinsFontFamily,
            color = Color.DarkGray
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = poppinsFontFamily,
            color = color
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}

// You can add this to your existing navigation graph
/*
@Composable
fun YourNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "taskDetail") {
        composable("taskDetail") {
            TaskDetailScreen(
                taskId = 123,
                name = "John Doe",
                imageUrl = null, // Replace with actual image URL if available
                heading = "Project Alpha",
                startDate = "2024-05-01",
                status = "Incomplete",
                dueDate = "2024-06-30",
                description = "Design and implement the new task management module.",
                assignedUser = "Alice Smith",
                navController = navController
            )
        }
        composable("attendanceScreen") {
            MonthlyAttendanceScreen(navController = navController)
        }
        // ... other routes
    }
}
*/