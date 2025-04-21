package com.fourpixel.fourpixelhrapplication

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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fourpixel.fourpixelhrapplication.ui.theme.poppinsFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }

    val allTasks = listOf(
        Task("P001-02", "Task Page Design", "Mobile App UI", "Ongoing", "5 Days left"),
        Task("P001-03", "Holiday Page Design", "Mobile App UI", "Pending", "14 Days left"),
        Task("P001-01", "Login Page Design", "Mobile App UI", "Completed", "Completed on 2024.11.10")
    )

    val filteredTasks = when (selectedFilter) {
        "All" -> allTasks
        else -> allTasks.filter { it.status == selectedFilter }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Header Row
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
            }
            Text(
                text = "Tasks",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Search
        @Composable
        fun SearchBar(searchQuery: String, onSearchQueryChange: (String) -> Unit) {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = { Text("Search", fontFamily = poppinsFontFamily) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear Search")
                        }
                    }
                },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF5F5F5),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All", "Ongoing", "Pending", "Completed").forEach { status ->
                FilterChip(
                    text = status,
                    selected = selectedFilter == status,
                    onClick = { selectedFilter = status }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Task Cards
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            filteredTasks.forEach { task ->
                TaskCard(task)
            }
        }
    }
}

@Composable
fun FilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
    val bgColor = if (selected) Color.Black else Color.LightGray.copy(alpha = 0.3f)
    val contentColor = if (selected) Color.White else Color.Black

    Text(
        text = text,
        fontFamily = poppinsFontFamily,
        fontSize = 14.sp,
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() },
        color = contentColor
    )
}

@Composable
fun TaskCard(task: Task) {
    val backgroundColor = when (task.status) {
        "Ongoing" -> Color(0xFFDCEEFB)
        "Pending" -> Color(0xFFFFF9C4)
        "Completed" -> Color(0xFFE8F5E9)
        else -> Color.White
    }
    val statusColor = when (task.status) {
        "Ongoing" -> Color(0xFF3B82F6)
        "Pending" -> Color(0xFFFFC107)
        "Completed" -> Color(0xFF4CAF50)
        else -> Color.Gray
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = task.status,
            color = statusColor,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            fontFamily = poppinsFontFamily
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = task.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFontFamily
        )

        Text(
            text = task.subtitle,
            fontSize = 14.sp,
            fontFamily = poppinsFontFamily,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = task.due,
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = poppinsFontFamily
            )
            Spacer(modifier = Modifier.weight(1f))
            CircleInitial("J")
        }
    }
}

@Composable
fun CircleInitial(initial: String) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .background(Color.Black, shape = RoundedCornerShape(50)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            color = Color.White,
            fontSize = 14.sp,
            fontFamily = poppinsFontFamily
        )
    }
}

data class Task(
    val id: String,
    val title: String,
    val subtitle: String,
    val status: String,
    val due: String
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TaskListScreenPreview() {

        TaskListScreen()

}


