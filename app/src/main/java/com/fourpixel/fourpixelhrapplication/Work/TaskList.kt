package com.fourpixel.fourpixelhrapplication.Work

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fourpixel.fourpixelhrapplication.client.Task
import com.fourpixel.fourpixelhrapplication.ui.theme.poppinsFontFamily
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(navController: NavController, viewModel: TaskListViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val filteredTasks by viewModel.filteredTasks.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Header Row
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
            }
            Text(
                text = "Tasks",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            )

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {navController.navigate("addTask")},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF9C75A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("+ ADD Task ", fontFamily = poppinsFontFamily)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Search Bar
        SearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { viewModel.updateSearchQuery(it) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Buttons
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listOf("All", "Ongoing", "Pending", "Completed")) { status ->

                FilterChip(
                    text = status,
                    selected = selectedFilter == status,
                    onClick = { viewModel.updateFilter(status) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Task Cards
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            filteredTasks.forEach { task ->
                TaskCard(task = task) {
                    navController.navigate(
                        "taskDetail/${task.id}/${Uri.encode(task.heading)}/${task.status}/${Uri.encode(task.dueDate ?: "N/A")}/${Uri.encode(task.assignedUser ?: "N/A")}"
                    )
                }
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

@OptIn(ExperimentalMaterial3Api::class)
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


@Composable
fun TaskCard(task: Task,onClick: () -> Unit) {
    val displayStatus = when (task.status.lowercase()) {
        "incomplete" -> "Ongoing"
        "on hold" -> "Pending"
        "completed" -> "Completed"
        else -> task.status
    }

    val backgroundColor = when (displayStatus) {
        "Ongoing" -> Color(0xFFDCEEFB)
        "Pending" -> Color(0xFFFFF9C4)
        "Completed" -> Color(0xFFE8F5E9)
        else -> Color.White
    }

    val statusColor = when (displayStatus) {
        "Ongoing" -> Color(0xFF3B82F6)
        "Pending" -> Color(0xFFFFC107)
        "Completed" -> Color(0xFF4CAF50)
        else -> Color.Gray
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .padding(16.dp).clickable { onClick() }
    ) {
        Text(
            text = displayStatus,
            color = statusColor,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            fontFamily = poppinsFontFamily
        )
        Spacer(modifier = Modifier.height(4.dp))
        Divider(color = Color(0xFFDADADA), thickness = 1.dp)

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = task.heading,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFontFamily
        )

        Text(
            text = "Task ID: ${task.id}",
            fontSize = 14.sp,
            fontFamily = poppinsFontFamily,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Calendar",
                modifier = Modifier.size(16.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = task.dueDate ?: "No due date",
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = poppinsFontFamily
            )
            Spacer(modifier = Modifier.weight(1f))
            CircleInitial("S")
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
    val id: Int,
    val heading: String,
    val status: String,
    val dueDate: String?,
    val projectName: String?,
    val assignedUser: String?
)




