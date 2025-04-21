package com.fourpixel.fourpixelhrapplication.HR

import androidx.compose.foundation.border
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fourpixel.fourpixelhrapplication.ui.theme.poppinsFontFamily

@Composable
fun LeavesScreen(viewModel: LeavesViewModel = viewModel()) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val filteredLeaves by viewModel.filteredLeaves.collectAsState()

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
                text = "Leaves",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF9C75A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("+ Apply Leave", fontFamily = poppinsFontFamily)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        SearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { viewModel.updateSearchQuery(it) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All", "Annual", "Casual", "Medical").forEach { type ->
                val count = viewModel.getCount(type)
                FilterChip(
                    text = "$type ($count)",
                    selected = selectedFilter == type,
                    onClick = { viewModel.updateFilter(type) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            filteredLeaves.forEach { (month, leaves) ->
                Text(
                    text = month,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFontFamily,
                    color = Color.Gray
                )
                leaves.forEach { leave ->
                    LeaveCard(leave)
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
fun LeaveCard(leave: Leave) {
    val bgColor = when (leave.status) {
        "Rejected" -> Color(0xFFFFEBEE)
        else -> Color(0xFFF6F6F6)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(leave.title, fontSize = 12.sp, fontFamily = poppinsFontFamily, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(leave.date, fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = poppinsFontFamily)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .background(
                        color = when (leave.type) {
                            "Medical" -> Color(0xFFF9C75A)
                            "Casual" -> Color(0xFFB2CC8E)
                            "Annual" -> Color(0xFFD7BE9D)
                            else -> Color.LightGray
                        },
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(text = leave.type, fontSize = 12.sp, fontFamily = poppinsFontFamily, color = Color.Black)
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .background(
                        color = if (leave.status == "Rejected") Color.Transparent else Color.White,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = if (leave.status == "Rejected") Color.Red else Color.Black,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = leave.status,
                    fontSize = 12.sp,
                    color = if (leave.status == "Rejected") Color.Red else Color.Black,
                    fontFamily = poppinsFontFamily
                )
            }
        }
    }
}


data class Leave(
    val title: String,
    val date: String,
    val type: String,
    val status: String,
    val monthYear: String
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LeavesScreenPreview() {
    LeavesScreen()
}
