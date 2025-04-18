package com.fourpixel.fourpixelhrapplication.DashBoardSection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavController
import com.fourpixel.fourpixelhrapplication.DialogWithImage
import com.fourpixel.fourpixelhrapplication.R
import com.fourpixel.fourpixelhrapplication.sideDrawer
import coil.compose.rememberAsyncImagePainter


import com.fourpixel.fourpixelhrapplication.ui.theme.poppinsFontFamily
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardView(navController: NavController, userName: String, userImageUrl: String){


    val viewModel: DashboardViewModelJP = androidx.lifecycle.viewmodel.compose.viewModel()

    LaunchedEffect(userName) {
        viewModel.setUserName(userName)
    }

    val displayedUserName by viewModel.userName.collectAsState()

    val isRunning by viewModel.isRunning.collectAsState()
    val elapsedTime by viewModel.elapsedTime.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    //Variables used for showing Date
    val currentDate = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("EEEE, MMMM d'th', yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(currentDate)


    //Clock out Popup
    val showDialog by viewModel.showDialog.collectAsState()

    var isSelectionMade by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                sideDrawer(userName, userImageUrl)
            }
        }
    ) {


    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        //Icons on Top
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                modifier = Modifier
                    .background(Color(0xFFF2F2F2), shape = RoundedCornerShape(4.dp))

            ) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
            }
            Row (
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(onClick = { /* Function to Show Notifications */ },
                    modifier = Modifier
                        .background(Color(0xFFF2F2F2), shape = CircleShape)) {
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications")
                }
                Image(
                    painter = rememberAsyncImagePainter(userImageUrl),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { /* Handle profile click */ },
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        //Dashboard Text
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Welcome, $displayedUserName 👋",
                fontFamily = poppinsFontFamily,
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),

                )

        }

        Spacer(modifier = Modifier.height(6.dp))

        //Date Details
        Column(modifier = Modifier.align(Alignment.Start)) {
            TodayDateDisplay()
        }


        Spacer(modifier = Modifier.height(30.dp))


        // Timer Circle
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(250.dp)
        ) {
            //Circle to get outer gold line
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .background(Color(0xFFFFC107), shape = CircleShape)
            )

            //Circle to get the white space between two circles
            Box(
                modifier = Modifier
                    .size(247.dp)
                    .background(Color.White, shape = CircleShape)
            )

            //Clock Circle
            Box(
                modifier = Modifier
                    .size(225.dp)
                    .background(
                        if (isRunning) Color(0xFFFCE7C2) else Color(0xFFF5F5F5),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val hours = (elapsedTime / 3600).toString().padStart(2, '0')
                    val minutes = ((elapsedTime % 3600) / 60).toString().padStart(2, '0')
                    val seconds = (elapsedTime % 60).toString().padStart(2, '0')




                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$hours:$minutes",
                            fontSize = 48.sp,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                    }
                    Text(
                        text =":$seconds",
                        fontSize = 24.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray)

                    Text(
                        text = if (isRunning) "Working" else "Ready",
                        fontSize = 16.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray

                    )

                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        DropdownMenu(viewModel) { selected ->
            isSelectionMade = selected
        }

        Spacer(modifier = Modifier.height(10.dp))

// Clock-in and Clock-out Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { viewModel.toggleClockIn() },
                enabled = isSelectionMade,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) Color.Gray else Color(0xFFFFC107)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text(
                    text = "Clock-in",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Button(
                onClick = { viewModel.toggleClockOut() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isRunning) Color.Gray else Color(0xFFFFC107)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text(
                    text = "Clock-out",
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily,
                    fontSize = 18.sp
                )
            }}
        if (showDialog) {
            DialogWithImage(
                onDismissRequest = { viewModel.dismissDialog() },
                onConfirmation = { viewModel.dismissDialog() },
                painter = painterResource(id = R.drawable.wrapup),
                imageDescription = "Clock-out confirmation"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Status
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Status",
                fontFamily = poppinsFontFamily,
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),

                )

        }
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatusCard("Assigned", "Projects", "03", Color.Black)
            Spacer(modifier = Modifier.width(10.dp))
            StatusCard("Pending", "Tasks", "10", Color(0xFF88B04B))
        }
    }
}}

@Composable
fun StatusCard(title: String, subtitle: String, count: String, color: Color) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(160.dp)
        ,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = title,fontFamily = poppinsFontFamily, color = Color(0xFFDADADA), fontSize = 12.sp)
                Text(text = subtitle,fontFamily = poppinsFontFamily, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Text(
                text = count,
                color = Color.White,
                fontSize = 40.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.End).padding(end = 16.dp))
        }
    }
}

@Composable
fun DropdownMenu(viewModel: DashboardViewModelJP, onSelectionMade: (Boolean) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var displayText by remember { mutableStateOf("Select Work Mode") }
    val selectedOption by viewModel.selectedOption.collectAsState()
    val options = listOf("Office", "Work from Home")

    var dropdownWidth by remember { mutableStateOf(0) }
    var isSelected by remember { mutableStateOf(false) } // Track if an option is selected

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp))
                .clickable { expanded = true }
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .onGloballyPositioned { coordinates ->
                    dropdownWidth = coordinates.size.width
                }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = displayText,
                    fontFamily = poppinsFontFamily,
                    color = if (isSelected) Color.Black else Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                    tint = Color.Gray
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { dropdownWidth.toDp() })
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, fontFamily = poppinsFontFamily) },
                    onClick = {
                        viewModel.setSelectedOption(option)
                        displayText = option
                        isSelected = true
                        expanded = false
                        onSelectionMade(true)  // Notify parent that a selection was made
                    }
                )
            }
        }
    }
}

@Composable
fun TodayDateDisplay() {
    val currentDate = Calendar.getInstance()

    // Format without the day (to manually handle the suffix)
    val dateFormat = SimpleDateFormat("EEEE, MMMM", Locale.getDefault())
    val day = currentDate.get(Calendar.DAY_OF_MONTH)
    val year = currentDate.get(Calendar.YEAR)

    // Get correctly formatted day with suffix
    val formattedDate = "${dateFormat.format(currentDate.time)} ${getDayWithSuffix(day)}, $year"

    Text(
        text = "Today, $formattedDate",
        fontFamily = poppinsFontFamily,
        style = TextStyle(fontSize = 12.sp, color = Color.Gray)
    )
}

// Function to get the correct suffix for the date
fun getDayWithSuffix(day: Int): String {
    return when {
        day in 11..13 -> "$day" + "th"  // 11th, 12th, 13th are always "th"
        day % 10 == 1 -> "$day" + "st"  // 1st, 21st, 31st
        day % 10 == 2 -> "$day" + "nd"  // 2nd, 22nd
        day % 10 == 3 -> "$day" + "rd"  // 3rd, 23rd
        else -> "$day" + "th"           // 4th, 5th, ..., 10th, etc.
    }
}

