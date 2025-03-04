package example.com.fourpixelhrapplication

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.gcm.Task
import example.com.fourpixelhrapplication.ui.theme.poppinsFontFamily

@Composable
fun TaskListScreen(){
    //Search Bar Variable
    var searchQuery by remember { mutableStateOf("") }


    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        //Header and Buttons
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
                text = "Tasks",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.width(80.dp))

            //Button
            /* Button is not centered with other items. Hence commented out.
            Button(onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.width(160.dp).height(48.dp))
            {
                Text(
                    text = "+ Add Task",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

            }*/

        }

        //Search Bar
        SearchBar(searchQuery = searchQuery) {searchQuery = it }



    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchQuery: String, onSearchQueryChange: (String) -> Unit) {
    TextField(
        value = searchQuery,
        onValueChange = { onSearchQueryChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
        placeholder = { Text("Search",fontFamily = poppinsFontFamily) },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Clear Search")
                }
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            containerColor =  Color(0xFFF5F5F5),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}




@Preview(showBackground = true)
@Composable
fun TaskListScreenPreview() {
    TaskListScreen()
}
