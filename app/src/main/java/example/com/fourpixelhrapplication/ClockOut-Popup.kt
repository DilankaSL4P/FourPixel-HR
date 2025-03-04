package example.com.fourpixelhrapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import example.com.fourpixelhrapplication.ui.theme.poppinsFontFamily

@Composable
fun DialogWithImage(
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
                .height(500.dp)
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

                Spacer(modifier = Modifier.height(24.dp))

                //Text
                Text(
                    text = "Time to Wrap Up!",
                    fontFamily = poppinsFontFamily,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

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

                Spacer(modifier = Modifier.height(16.dp))

                //Clock-Out Button
                Button(
                        onClick = { isRunning = false },
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


@Preview(showBackground = true)
@Composable
fun DialogWithImagePreview() {
    DialogWithImage(
        onDismissRequest = {},
        onConfirmation = {},
        painter = painterResource(id = R.drawable.wrapup),
        imageDescription = "Holiday image"
    )
}





