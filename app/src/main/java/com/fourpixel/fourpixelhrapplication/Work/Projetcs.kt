package com.fourpixel.fourpixelhrapplication.Work


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.navigation.compose.rememberNavController
import com.fourpixel.fourpixelhrapplication.client.Project
import com.fourpixel.fourpixelhrapplication.ui.theme.poppinsFontFamily
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListScreen(navController: NavController, viewModel: ProjectsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val filteredProjects by viewModel.filteredProjects.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
            }
            Text(
                text = "Projects",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            )
            Spacer(modifier = Modifier.weight(1f))

        }

        Spacer(modifier = Modifier.height(8.dp))

        SearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { viewModel.updateSearchQuery(it) }
        )

        Spacer(modifier = Modifier.height(12.dp))

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

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            filteredProjects.forEach { project ->
                ProjectCard(project)
            }
        }
    }
}

@Composable
fun ProjectCard(project: Project) {

    val displayStatus = when (project.status.lowercase()) {
        "in progress" -> "Ongoing"
        "on hold" -> "Pending"
        "completed" -> "Completed"
        else -> project.status
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
    Card(modifier = Modifier
        .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(2.dp)) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = displayStatus,
                color = statusColor,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                fontFamily = poppinsFontFamily
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Project ID: ${project.id}",
                fontSize = 14.sp,
                fontFamily = poppinsFontFamily,
                color = Color.Gray
            )}

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color(0xFFDADADA), thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = project.projectName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Project ID: ${project.id}",
                fontSize = 14.sp,
                fontFamily = poppinsFontFamily,
                color = Color.Gray
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Calendar",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Deadline: ${project.deadline ?: "N/A"}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = poppinsFontFamily
                )
                Spacer(modifier = Modifier.weight(1f))
                CircleInitial("S")
            }


        }

    }
}

data class Project(
    val id: Int,
    val project_name: String,
    val start_date: String,
    val deadline: String?,
    val status: String,
    val company_id: Int
)



