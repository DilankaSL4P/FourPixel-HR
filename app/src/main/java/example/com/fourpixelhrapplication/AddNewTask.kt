package example.com.fourpixelhrapplication

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
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
import example.com.fourpixelhrapplication.ui.theme.poppinsFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTask(modifier: Modifier){
    var project by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var taskCategory by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("") }

    //Popup calender Variables
    val calendar = Calendar.getInstance()

    //Date selecting dialog
    val startDatePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            startDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    val endDatePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            endDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )


    Column(
        modifier = Modifier.fillMaxWidth().padding(32.dp),
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
                    contentDescription = "Go Back ",
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            //Header
            Text(
                text = "Add New Task",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily,
                modifier = Modifier.padding(bottom = 16.dp)
            )

        }

        Spacer(modifier = Modifier.height(8.dp))

        //Should be a drop down menu
        OutlinedTextField(
            value = project,
            onValueChange = { project = it },
            label = { Text("Project",
                fontFamily = poppinsFontFamily,
                color = Color.DarkGray) },
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
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        //Title Section
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title",
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

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        //Description Section
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description",
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

        Spacer(modifier = Modifier.height(8.dp))

        //Start Date Section
        //The Popup Calendar Theme Should be changed
        OutlinedTextField(
            value = startDate,
            onValueChange = { startDate = it },
            label = { Text("Start Date",fontFamily = poppinsFontFamily,
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
                    modifier = Modifier.clickable { startDatePickerDialog.show() }
                )
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        //End Date Section
        OutlinedTextField(
            value = endDate,
            onValueChange = { endDate = it },
            label = { Text("End Date",fontFamily = poppinsFontFamily,
                color = Color.Gray) },
            textStyle = TextStyle(color = Color.DarkGray, fontFamily = poppinsFontFamily),
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
                    modifier = Modifier.clickable { endDatePickerDialog.show() }
                )
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        //Task Category Section - should be a dropdown menu
        OutlinedTextField(
            value = taskCategory,
            onValueChange = { taskCategory = it },
            label = { Text("Task Category",
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
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        //Status Section - should be a dropdown menu
        OutlinedTextField(
            value = status,
            onValueChange = { status = it },
            label = { Text("Status",
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
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        //Priority Section - should be a dropdown menu
        OutlinedTextField(
            value = priority,
            onValueChange = { priority = it },
            label = { Text("Priority",
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
            modifier = Modifier.fillMaxWidth()
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
fun AddTaskPreview() {
    AddTask(modifier = Modifier)
}