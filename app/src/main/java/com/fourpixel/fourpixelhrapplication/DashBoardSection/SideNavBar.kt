package com.fourpixel.fourpixelhrapplication.DashBoardSection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.fourpixel.fourpixelhrapplication.ui.theme.poppinsFontFamily

data class MenuItem(val label: String, val children: List<String> = emptyList())

@Composable
fun sideDrawer(navController: NavController, userName: String, userImageUrl: String, userRole: String) {

    val menuItems = listOf(
        MenuItem("Dashboard"),
        MenuItem("Work", listOf("Projects", "Tasks",)),
        MenuItem("HR", listOf("Leaves", "Attendance","Holiday", "Appreciation")),
        MenuItem("Finance", listOf("Expenses", "Pay Sheets")),
        MenuItem("About"),
        MenuItem("Logout")
    )

    val expandedWork = remember { mutableStateOf(false) }
    val expandedHR = remember { mutableStateOf(false) }
    val expandedFinance = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(24.dp))
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Black, shape = CircleShape)
                    .wrapContentSize(Alignment.Center)
            ) {
                // Show first letter of name
                Image(
                    painter = rememberAsyncImagePainter(userImageUrl),
                    contentDescription = "Profile Image in Drawer",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.LightGray, CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(18.dp))
            Column {
                Text(
                    userName,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    userRole,
                    fontFamily = poppinsFontFamily,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        menuItems.forEach { item ->
            if (item.label == "Logout") {
                Spacer(modifier = Modifier.height(24.dp))
            }

            val isExpandable = item.children.isNotEmpty()
            val isExpanded = when (item.label) {
                "Work" -> expandedWork.value
                "HR" -> expandedHR.value
                "Finance" -> expandedFinance.value
                else -> false
            }

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            when (item.label) {
                                "Work" -> expandedWork.value = !expandedWork.value
                                "HR" -> expandedHR.value = !expandedHR.value
                                "Finance" -> expandedFinance.value = !expandedFinance.value
                                else -> Unit
                            }
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.label,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFontFamily
                    )
                    if (isExpandable) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Outlined.KeyboardArrowDown else Icons.Outlined.KeyboardArrowRight,
                            contentDescription = "Expand/Collapse",
                            tint = Color.Black
                        )
                    }
                }

                if (isExpanded) {
                    item.children.forEach { child ->
                        Text(
                            text = child,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = poppinsFontFamily,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 36.dp, top = 2.dp, bottom = 4.dp)
                                .clickable {
                                    when (child) {
                                        "Tasks" -> navController.navigate("tasks")
                                        "Projects" -> navController.navigate("projects")

                                    }
                                }
                        )
                    }
                }
            }
        }
    }
}
