package com.fourpixel.fourpixelhrapplication.Work

import android.content.res.Configuration
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fourpixel.fourpixelhrapplication.ui.theme.poppinsFontFamily

@Composable
fun TaskDetailScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { /* TODO */ }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
            }
            Text(
                text = "Task #P001-02",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            )
            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { /* TODO */ }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More")
            }
        }
        Spacer(modifier = Modifier.height(30.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "Ongoing",
                color = Color(0xFF3B82F6),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = poppinsFontFamily
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Title and Subtitle
        Text(
            text = "Mobile App UI",
            fontSize = 14.sp,
            color = Color.Gray,
            fontFamily = poppinsFontFamily
        )
        Text(
            text = "Task Page Design",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFontFamily
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Description and Info Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF2F2F2), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Description",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                fontFamily = poppinsFontFamily
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that",
                fontSize = 14.sp,
                color = Color.DarkGray,
                fontFamily = poppinsFontFamily
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Column {
                    Text(
                        text = "Created Date",
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFontFamily
                    )
                    Text(
                        text = "20th Oct 2024",
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.width(32.dp))
                Column {
                    Text(
                        text = "Due Date",
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFontFamily
                    )
                    Text(
                        text = "10th Nov 2024",
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Assigned to",
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.Black, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "J",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFontFamily
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Jhon Doe",
                    fontSize = 14.sp,
                    fontFamily = poppinsFontFamily
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bottom Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black, RoundedCornerShape(16.dp))
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomBarItem(icon = Icons.Default.List, label = "Sub Task", selected = true)
            BottomBarItem(icon = Icons.Default.ChatBubbleOutline, label = "Comment")
            BottomBarItem(icon = Icons.Default.Note, label = "Notes")
            BottomBarItem(icon = Icons.Default.AccessTime, label = "Timesheet")
            BottomBarItem(icon = Icons.Default.UploadFile, label = "Files")
        }
    }
}

@Composable
fun BottomBarItem(icon: ImageVector, label: String, selected: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) Color(0xFFF9C75A) else Color.White,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (selected) Color(0xFFF9C75A) else Color.White,
            fontFamily = poppinsFontFamily
        )
    }
}


@Preview(showBackground = true, name = "Light Mode")
@Composable
fun TaskDetailPreview() {
    MaterialTheme {
        TaskDetailScreen()
    }
}