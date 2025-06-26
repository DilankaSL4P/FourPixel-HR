package com.fourpixel.fourpixelhrapplication.dashboardsection


/*@Composable
fun ClockOutDialog(
    viewModel: DashboardViewModelJP,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    painter: Painter,
    imageDescription: String,
) {
    var isRunning by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
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
                //Close Button
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
                            isRunning = false
                            onConfirmation()
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






*/
