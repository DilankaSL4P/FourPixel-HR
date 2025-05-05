package com.fourpixel.fourpixelhrapplication.HR

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fourpixel.fourpixelhrapplication.DashBoardSection.getDayWithSuffix
import com.fourpixel.fourpixelhrapplication.ui.theme.poppinsFontFamily
import com.fourpixel.fourpixelhrapplication.client.Leave
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LeavesScreen(
    navController: NavController,
    viewModel: LeavesViewModel = viewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val filteredLeavesByMonth by viewModel.filteredLeavesByMonth.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
            }
            Text(
                text = "Leaves",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { /* Navigate to Apply Leave */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF9C75A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("+ Apply Leave", fontFamily = poppinsFontFamily)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Search
        SearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { viewModel.updateSearchQuery(it) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Chips
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filters = listOf("All") + listOf("Annual", "Casual", "Medical")
            filters.forEach { filter ->
                val count = viewModel.getCount(filter)
                FilterChip(
                    text = "$filter ($count)",
                    selected = selectedFilter == filter,
                    onClick = { viewModel.updateFilter(filter) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Error or loading state
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )
        }

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }

        if (filteredLeavesByMonth.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No leaves found", color = Color.Gray)
            }
            return
        }

        // Grouped Leave List
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            filteredLeavesByMonth.forEach { (monthYear, leaves) ->
                item {
                    Text(
                        text = monthYear,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = Color.Gray,
                        fontFamily = poppinsFontFamily
                    )
                }
                items(leaves) { leave ->
                    LeaveCard(leave = leave)
                }
            }
        }
    }
}


@Composable
fun FilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
    val bgColor = if (selected) Color.Black else Color.LightGray.copy(alpha = 0.3f)
    val textColor = if (selected) Color.White else Color.Black

    Text(
        text = text,
        color = textColor,
        fontFamily = poppinsFontFamily,
        fontSize = 14.sp,
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() }
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
                    Icon(Icons.Default.Close, contentDescription = "Clear Search")
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
fun LeaveCard(leave: Leave) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val displayFormat = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
    val formattedDate = try {
        dateFormat.parse(leave.leaveDate)?.let { displayFormat.format(it) } ?: leave.leaveDate
    } catch (e: Exception) {
        leave.leaveDate
    }

    val statusColor = when (leave.status.lowercase()) {
        "approved" -> Color.Black
        "rejected" -> Color.Red
        else -> Color.Gray
    }

    val (typeColor, typeName) = when (leave.leaveTypeId) {
        1 -> Pair(Color(0xFF88B04B), "Casual")
        2 -> Pair(Color(0xFFF9B232), "Medical")
        3 -> Pair(Color(0xFFD8A568), "Annual")
        else -> Pair(Color.LightGray, leave.leaveTypeId)
    }

    val leaveDuration = when (leave.duration){
        "single" -> "One Day Leave"
        "half day" -> "Half Day Leave"
        else -> "Multiple Days"
    }

    val bgColor = if (leave.status.lowercase() == "rejected") Color(0xFFFFEBEE) else Color(0xFFF6F6F6)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = leaveDuration,
            fontSize = 12.sp,
            color = Color.Gray,
            fontFamily = poppinsFontFamily
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = formattedDate,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFontFamily
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(typeColor, RoundedCornerShape(6.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {

                Text(
                    text = typeName.toString(),
                    fontSize = 12.sp,
                    fontFamily = poppinsFontFamily,
                    color = Color.Black
                )
            }

            Box(
                modifier = Modifier
                    .border(1.dp, statusColor, RoundedCornerShape(6.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = leave.status.replaceFirstChar { it.uppercase() },
                    fontSize = 12.sp,
                    fontFamily = poppinsFontFamily,
                    color = statusColor
                )
            }
        }
    }
}

