package example.com.fourpixelhrapplication.ui.theme
import androidx.compose.material.icons.filled.Fingerprint
import example.com.fourpixelhrapplication.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp






@Composable
fun LoginScreen(){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isEmailFocused by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }


    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }


    //Fill area
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        //Logo placement
        Image(
            painter = painterResource(id = R.drawable.four_pixel), // Replace with your new logo name
            contentDescription = "Four Pixel",
            modifier = Modifier.size(200.dp)
        )

        //Header
        Text(text = "Login", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Text(text = "Enter Your Credentials to login", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.Gray)

        Spacer(modifier = Modifier.height(20.dp))

        //Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address", fontSize = 12.sp) },
            modifier = Modifier.fillMaxWidth().height(if (isEmailFocused) 100.dp else 60.dp) ,
            shape = RoundedCornerShape(14.dp),
            textStyle = TextStyle(
                fontSize = 16.sp, // Increase font size
                lineHeight = 20.sp // Increase line height
            ),
        )

        Spacer(modifier = Modifier.height(18.dp))

        //Password
        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            modifier = Modifier.fillMaxWidth().height(if (isEmailFocused) 100.dp else 60.dp) ,
            shape = RoundedCornerShape(14.dp),
            textStyle = TextStyle(
                fontSize = 16.sp, // Increase font size
                lineHeight = 20.sp // Increase line height
            ),
            label = { Text("Password",fontSize = 12.sp) },
            trailingIcon = {
                val image = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = {showPassword = !showPassword}) {
                    Icon(imageVector = image, contentDescription = "Toggled password visibility")
                }
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)

        )
        Spacer(modifier = Modifier.height(24.dp))

        //LoginButton
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDB833))

        ){
            Text(text = "Login", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "or use Biometrics", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(70.dp) // Adjust the outer size for better padding
                .background(Color(0xFFF2F2F2), shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Filled.Fingerprint,
                contentDescription = "Fingerprint Authentication",
                modifier = Modifier.size(50.dp),
                tint = Color.DarkGray
            )
        }
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Sign in to different domain",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.clickable { /* Handle domain sign-in */ }
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(text = "Powered by", fontSize = 14.sp, color = Color.Gray)
        //Bsuite Logo

    }

}

