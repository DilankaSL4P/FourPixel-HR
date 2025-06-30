package com.fourpixel.fourpixelhrapplication.notices

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fourpixel.fourpixelhrapplication.client.Notice
import com.fourpixel.fourpixelhrapplication.features.noticeboard.NoticeViewModel
import com.fourpixel.fourpixelhrapplication.ui.theme.ArcSpinner
import com.fourpixel.fourpixelhrapplication.ui.theme.poppinsFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeBoardScreen(
    navController: NavController,
    viewModel: NoticeViewModel = viewModel()
) {

    val notices by viewModel.notices.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()


    val canNavigateBack = navController.previousBackStackEntry != null

    Column(
        modifier = Modifier
            .fillMaxSize()

            .padding(vertical = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(24.dp))
        TopAppBar(
            title = {
                Text(
                    text = "Notice Board",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily
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

            modifier = Modifier.padding(horizontal = 16.dp)
        )


        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                ArcSpinner()
            }  else if (notices.isEmpty()) {
                Text("No notices available at the moment.")
            } else {
                LazyColumn(

                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
                ) {
                    items(notices) { notice ->
                        NoticeCard(notice)
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

        Text(
            text = notice.description ?: "No description",
            fontSize = 14.sp,
            fontFamily = poppinsFontFamily,
            color = Color.DarkGray
        )
    }
}