package example.com.fourpixelhrapplication.ui.theme

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import example.com.fourpixelhrapplication.AddTask
import example.com.fourpixelhrapplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyLeave(){
    var leaveType by remember { mutableStateOf("") }
    var leaveDate by remember { mutableStateOf("") }
    var leaveReason by remember { mutableStateOf("") }
    val calendar = Calendar.getInstance()


    //Date selecting dialog
    val leaveDateDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            leaveDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        //Button and Header
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            //Back Button
            IconButton(
                onClick = {  },
                modifier = Modifier.size(28.dp)

            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Go Back",
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            //Header
            Text(
                text = "Leaves",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily,
                modifier = Modifier.padding(bottom = 16.dp)
            )

        }
        Spacer(modifier = Modifier.height(8.dp))

        //Leave Type Section - Should be a drop down menu
        //Dropdown menu items are not known
        OutlinedTextField(
            value = leaveType,
            onValueChange = { leaveType = it },
            label = { Text("Leave Type ",
                fontFamily = poppinsFontFamily,

                color = Color.DarkGray) },
            textStyle = TextStyle(
                fontFamily = poppinsFontFamily,
                color = Color.DarkGray
            ),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF2F2F2),
                focusedContainerColor = Color(0xFFF2F2F2),
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color.Black,
                cursorColor = Color.Black ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        //Leave Date Section
        //Calender UI needs to be changed
        OutlinedTextField(
            value = leaveDate,
            onValueChange = { leaveDate = it },
            label = { Text("Leave Date",fontFamily = poppinsFontFamily,
                color = Color.Gray) },
            textStyle = TextStyle(color = Color.DarkGray,fontFamily = poppinsFontFamily),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color.Black,
                cursorColor = Color.Black,
                containerColor = Color(0xFFF2F2F2)
            ),
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Select Date",
                    modifier = Modifier.clickable { leaveDateDialog.show() }
                )
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        //Reason Section
        OutlinedTextField(
            value = leaveReason,
            onValueChange = { leaveReason = it },
            label = { Text("Reason",
                fontFamily = poppinsFontFamily,
                color = Color.Gray) },
            //Input Text
            textStyle = TextStyle(
                fontFamily = poppinsFontFamily,
                color = Color.DarkGray
            ),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF2F2F2),
                focusedContainerColor = Color(0xFFF2F2F2),
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color.Black,
                cursorColor = Color.Black ),
            modifier = Modifier.fillMaxWidth().height(160.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Submit Button
        Button(
            onClick = {  },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)

        ) {
            Text(
                text = "Submit",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ApplyLeavePreview() {
    ApplyLeave()
}