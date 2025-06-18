package com.fourpixel.fourpixelhrapplication.DashBoardSection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.fourpixel.fourpixelhrapplication.R
import coil.compose.rememberAsyncImagePainter
import com.fourpixel.fourpixelhrapplication.ui.theme.poppinsFontFamily
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import android.Manifest
import android.content.IntentSender
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult // <--- ADD THIS IMPORT
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices


@Composable
fun DashboardView(navController: NavController, userName: String, userImageUrl: String, userRole: String) {
    val viewModel: DashboardViewModelJP = androidx.lifecycle.viewmodel.compose.viewModel()

    LaunchedEffect(userName) {
        viewModel.setUserName(userName)
    }

    val displayedUserName by viewModel.userName.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val elapsedTime by viewModel.elapsedTime.collectAsState()
    //val coroutineScope = rememberCoroutineScope()

    //val currentDate = Calendar.getInstance().time
    //val dateFormat = SimpleDateFormat("EEEE, MMMM 'th', yyyy", Locale.getDefault())
    //val formattedDate = dateFormat.format(currentDate)

    val showDialog by viewModel.showDialog.collectAsState()
    //var isSelectionMade by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val assignedProjects by viewModel.assignedProjects.collectAsState()
    val pendingTasks by viewModel.pendingTasks.collectAsState()

    val notices by viewModel.notices.collectAsState()
    var showNoticeBanner by remember { mutableStateOf(true) }
    var showNoticePopup by remember { mutableStateOf(false) }

    var displayText by remember { mutableStateOf("Work from") }

    val context = LocalContext.current
    val settingsClient = remember { LocationServices.getSettingsClient(context) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val selectedOption by viewModel.selectedOption.collectAsState()



    BackHandler {

    }
    //Forcing location permissions
    val resolutionForResult = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == android.app.Activity.RESULT_OK) {

            viewModel.handleClockInButtonClick(settingsClient = settingsClient,
                fusedLocationClient = fusedLocationClient,
                context = context)
        } else {
            // User did not agree to make required location settings changes
            Toast.makeText(context, "Location services not enabled. Cannot clock in.", Toast.LENGTH_LONG).show()
        }
    }

    //Launcher for requesting runtime location permissions
    val requestLocationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {

                Toast.makeText(context, "Precise location granted.", Toast.LENGTH_SHORT).show()
                viewModel.handleClockInButtonClick(settingsClient = settingsClient,
                    fusedLocationClient = fusedLocationClient,
                    context = context)
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted. Still sufficient for clock-in.
                Toast.makeText(context, "Approximate location granted.", Toast.LENGTH_SHORT).show()
                viewModel.handleClockInButtonClick(settingsClient = settingsClient,
                    fusedLocationClient = fusedLocationClient,
                    context = context)
            }
            else -> {
                // No location access granted.
                Toast.makeText(context, "Location permission denied. Cannot clock in.", Toast.LENGTH_LONG).show()
            }
        }
    }


    LaunchedEffect(viewModel) {
        viewModel.resolveLocationSettingsEvent.collect { resolvable ->
            try {
                resolutionForResult.launch(IntentSenderRequest.Builder(resolvable.resolution).build())
            } catch (sendEx: IntentSender.SendIntentException) {

                println("DEBUG: Error launching location settings resolution: ${sendEx.localizedMessage}")
            }
        }
    }


    LaunchedEffect(viewModel) {
        viewModel.showToastEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }



    //Function to request location permissions
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.entries.all { it.value }
        if (granted) {
            println("DEBUG: All location permissions granted.")
        } else {
            println("DEBUG: Location permissions denied. Clock-in features might be limited.")
        }
    }

    //Calling the location request function when Dashboard Loads
    LaunchedEffect(Unit) {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp)
            ) {
                SideDrawer(
                    navController = navController,
                    userName = userName,
                    userImageUrl = userImageUrl,
                    userRole = userRole,
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    //Navigation Drawer
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
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //Notifications
                        IconButton(
                            onClick = { /* Show notifications */ },
                            modifier = Modifier.background(Color(0xFFF2F2F2), shape = CircleShape)
                        ) {
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
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            //Notice Pop up
            if (notices.isNotEmpty() && showNoticeBanner) {
                item {
                    NoticeBanner(notice = notices.toString()) {
                        showNoticePopup = true
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            //Welcome Text
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Welcome, $displayedUserName !",
                        fontFamily = poppinsFontFamily,
                        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(6.dp)) }

            //Calling the Date Display Function
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    TodayDateDisplay()
                }
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }

            //Clock
            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(250.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(250.dp)
                            .background(Color(0xFFFFC107), shape = CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(247.dp)
                            .background(Color.White, shape = CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(225.dp)
                            .background(
                                if (isRunning) Color(0xFFFCE7C2) else Color(0xFFF5F5F5),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                                text = ":$seconds",
                                fontSize = 24.sp,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                            )
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
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }

            item {
                DropdownMenu(viewModel)
            }

            item { Spacer(modifier = Modifier.height(10.dp)) }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Clock-in Button
                    Button(
                        onClick = {
                            // Check for permissions first
                            val hasFineLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED
                            val hasCoarseLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED

                            if (hasFineLocation || hasCoarseLocation) {
                                viewModel.handleClockInButtonClick(
                                    settingsClient = settingsClient,
                                    fusedLocationClient = fusedLocationClient,
                                    context = context
                                )
                            } else {
                                requestLocationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            }
                        },
                        enabled = !isRunning && (selectedOption != "Working From"),
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

                    //ClockOut Button
                    Button(
                        onClick = { viewModel.showClockOutDialog() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!isRunning) Color.Gray else Color(0xFFFFC107)
                        ),
                        enabled = isRunning,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Clock-out",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            if (showDialog) {
                item {
                    ClockOutDialog(
                        viewModel = viewModel,
                        onDismissRequest = {

                            viewModel.dismissDialog()
                        },
                        onConfirmation = {
                            // Timer is stopped and the API call is made.
                            viewModel.confirmClockOut()
                        },
                        painter = painterResource(id = R.drawable.wrapup),
                        imageDescription = "Clock-out confirmation"
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Status",
                        fontFamily = poppinsFontFamily,
                        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(10.dp)) }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatusCard("Assigned", "Projects", assignedProjects.toString(), Color.Black)
                    Spacer(modifier = Modifier.width(10.dp))
                    StatusCard("Pending", "Tasks", pendingTasks.toString(), Color(0xFF88B04B))
                }
            }
        }

    }
    if (showNoticePopup) {
        NoticePopup(notice = notices.toString()) {
            showNoticePopup = false
            showNoticeBanner = false
        }
    }
}


//Project and Task Cards
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

//Work Mode Dropdown
@Composable
fun DropdownMenu(viewModel: DashboardViewModelJP) {
    var expanded by remember { mutableStateOf(false) }
    val selectedOption by viewModel.selectedOption.collectAsState()
    val options = listOf("Office", "Work from Home")

    var dropdownWidth by remember { mutableIntStateOf(0) }
    //val isSelected = selectedOption != "Work From"


    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {

        Button(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .onGloballyPositioned { coordinates ->
                    dropdownWidth = coordinates.size.width
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF5F5F5),
                contentColor = Color.Gray
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row( // Content of the button
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start, // Keep text left-aligned
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedOption,
                    fontFamily = poppinsFontFamily,
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

        // The actual DropdownMenu remains largely the same
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { dropdownWidth.toDp() })

        ) {
            options.forEach { option ->
                DropdownMenuItem(

                    text = {
                        Text(
                            option,
                            fontFamily = poppinsFontFamily,
                            color = Color.DarkGray
                        )
                    },
                    onClick = {
                        viewModel.setSelectedOption(option)
                        expanded = false
                    },

                    leadingIcon = {
                        if (option == "Office") {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray)
                        } else {
                            Icon(Icons.Default.Home, contentDescription = null, tint = Color.Gray)
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun NoticeBanner(notice: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF4E1), shape = RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(
            text = "🔔 New Notice Available - Tap to Read",
            color = Color.Black,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun NoticePopup(notice: String, onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = notice,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Text(
                text = notice,
                fontSize = 16.sp
            )
        },
        confirmButton = {
            Button(
                onClick = { onDismiss() }
            ) {
                Text("OK")
            }
        }
    )
}

// Date format
@Composable
fun TodayDateDisplay() {
    val currentDate = Calendar.getInstance()


    val dateFormat = SimpleDateFormat("EEEE, MMMM", Locale.getDefault())
    val day = currentDate.get(Calendar.DAY_OF_MONTH)
    val year = currentDate.get(Calendar.YEAR)


    val formattedDate = "${dateFormat.format(currentDate.time)} ${getDayWithSuffix(day)}, $year"

    Text(
        text = "Today, $formattedDate",
        fontFamily = poppinsFontFamily,
        style = TextStyle(fontSize = 12.sp, color = Color.Gray)
    )
}

// Date Suffix Filtering
fun getDayWithSuffix(day: Int): String {
    return when {
        day in 11..13 -> "$day" + "th"
        day % 10 == 1 -> "$day" + "st"
        day % 10 == 2 -> "$day" + "nd"
        day % 10 == 3 -> "$day" + "rd"
        else -> "$day" + "th"
    }
}


@Composable
fun ClockOutDialog(
    viewModel: DashboardViewModelJP,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    painter: Painter,
    imageDescription: String,
) {


    Dialog(onDismissRequest = { onDismissRequest() }) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(550.dp)
            ,
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            )
            {
                //Close X Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.size(24.dp)

                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                //Image
                Image(
                    painter = painterResource(id = R.drawable.wrapup),
                    contentDescription = imageDescription,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(220.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                //Text
                Text(
                    text = "Time to Wrap Up!",
                    fontFamily = poppinsFontFamily,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                //Body Text
                Text(
                    text = "Are you ready to clock out? Please confirm to finish up for today. Thank you for your hard work!",

                    color = Color.Gray,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    textAlign = TextAlign.Center)

                Spacer(modifier = Modifier.height(24.dp))

                //Clock-Out Button
                Button(
                    onClick = {

                        onConfirmation() // This will call viewModel.confirmClockOut
                    },
                    colors = ButtonDefaults.buttonColors(

                        containerColor =  Color(0xFFF9B232)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .height(40.dp).fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                ) {
                    Text(
                        text = "Clock-out",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}



