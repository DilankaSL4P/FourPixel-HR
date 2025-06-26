package com.fourpixel.fourpixelhrapplication.work // Or your UI package

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fourpixel.fourpixelhrapplication.client.Notice
// Make sure to import your ViewModel
import com.fourpixel.fourpixelhrapplication.features.noticeboard.NoticeViewModel
import com.fourpixel.fourpixelhrapplication.ui.theme.poppinsFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeBoardScreen(
    navController: NavController,
    // Use the NoticeViewModel we created
    viewModel: NoticeViewModel = viewModel()
) {
    // Collect all the states from the ViewModel
    val notices by viewModel.notices.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val canNavigateBack = navController.previousBackStackEntry != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            // Add vertical padding if needed, similar to what Scaffold might provide
            .padding(vertical = 16.dp)
    ) {
        // 1. TopAppBar Section
        // The Spacer and TopAppBar are now direct children of the Column.
        Spacer(modifier = Modifier.height(24.dp)) // Adjusted spacer as needed
        TopAppBar(
            title = {
                Text(
                    text = "Notice Board",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    // fontFamily = poppinsFontFamily // Uncomment if you have this font family
                )
            },
            navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
            // Modifiers for padding can be applied directly here
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // 2. Content Section
        Box(
            modifier = Modifier
                .weight(1f) // This makes the Box fill all available space in the Column
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center // Center the ProgressIndicator and Error messages
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            }  else if (notices.isEmpty()) {
                Text("No notices available at the moment.")
            } else {
                LazyColumn(
                    // The LazyColumn now fills the Box, which has the correct size
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
                ) {
                    items(notices) { notice ->
                        NoticeCard(notice) // Your item composable
                    }
                }
            }
        }
    }
}

@Composable
fun NoticeCard(notice: Notice) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFDCEEFB), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Text(
            text = notice.heading,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFontFamily
        )
        Spacer(modifier = Modifier.height(6.dp))
        // Use the official html converter from Android to display formatted text
        // For this, you would need to add a dependency and use an AndroidView
        // For simplicity, we'll just show the raw text.
        Text(
            text = notice.description ?: "No description",
            fontSize = 14.sp,
            fontFamily = poppinsFontFamily,
            color = Color.DarkGray
        )
    }
}