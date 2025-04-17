package com.fourpixel.fourpixelhrapplication

import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fourpixel.fourpixelhrapplication.ui.theme.poppinsFontFamily

@Composable
fun sideDrawer(userName: String, userImageUrl: String) {

    val menuItems = listOf(
        "Dashboard",
        "Projects",
        "Tasks",
        "Noticeboard",
        "Leaves",
        "Expense",
        "About",
        "Logout"
    )

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.White)
            .padding(32.dp)
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(24.dp))
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Black, shape = CircleShape)
                    .wrapContentSize(Alignment.Center)
            ) {
                // Show first letter of name
                Text(
                    userName.firstOrNull()?.uppercase() ?: "U",
                    fontFamily = poppinsFontFamily,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    userName,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    "Intern",
                    fontFamily = poppinsFontFamily,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))

        menuItems.forEach { label ->
            if (label == "Logout") {
                Spacer(modifier = Modifier.height(24.dp))
            }

            Text(
                text = label,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .clickable {
                        when (label) {
                            "Dashboard" -> Unit
                            "Projects" -> Unit
                            "Tasks" -> Unit
                            "Noticeboard" -> Unit
                            "Leaves" -> Unit
                            "Expense" -> Unit
                            "About" -> Unit
                            "Logout" -> Unit
                        }
                    }
            )
        }
    }
}



