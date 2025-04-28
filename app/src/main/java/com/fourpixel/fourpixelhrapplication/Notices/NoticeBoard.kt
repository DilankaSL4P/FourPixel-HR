package com.fourpixel.fourpixelhrapplication.Work

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavController
import com.fourpixel.fourpixelhrapplication.Notices.NoticeBoardViewModel
import com.fourpixel.fourpixelhrapplication.client.Notice
import com.fourpixel.fourpixelhrapplication.ui.theme.poppinsFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeBoardScreen(navController: NavController, viewModel: NoticeBoardViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val notices by viewModel.notices.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { /* Handle back */ }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
            }
            Text(
                text = "Notice Board",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            notices.forEach { notice ->
                NoticeCard(notice)
            }
        }
    }
}

@Composable
fun NoticeCard(notice: Notice) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
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
            text = notice.description,
            fontSize = 14.sp,
            fontFamily = poppinsFontFamily,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "To: ${notice.to}",
            fontSize = 12.sp,
            fontFamily = poppinsFontFamily,
            color = Color.Gray
        )
    }
}
