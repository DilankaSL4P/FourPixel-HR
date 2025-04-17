package com.fourpixel.fourpixelhrapplication.LoginSection

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fourpixel.fourpixelhrapplication.ui.theme.poppinsFontFamily
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import com.fourpixel.fourpixelhrapplication.R
import java.net.URLEncoder


@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val showPassword by viewModel.showPassword.collectAsState()
    val loginError by viewModel.loginError.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userName by viewModel.userName.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.four_pixel),
            contentDescription = "Four Pixel",
            modifier = Modifier.size(200.dp)
        )

        Text(text = "Login", fontSize = 24.sp, fontFamily = poppinsFontFamily, fontWeight = FontWeight.Bold)

        Text(
            text = "Enter your credentials to login",
            fontSize = 14.sp,
            color = Color.Gray,
            fontFamily = poppinsFontFamily,
            modifier = Modifier.padding(top = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        val focusManager = LocalFocusManager.current

        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.updateEmail(it) },
            label = { Text("Email Address", fontFamily = poppinsFontFamily) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next // Enables "Next" action on keyboard
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) } // Moves focus to password field
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.updatePassword(it) },
            label = { Text("Password", fontFamily = poppinsFontFamily) },
            trailingIcon = {
                val image = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { viewModel.toggleShowPassword() }) {
                    Icon(imageVector = image, contentDescription = "Toggle password visibility")
                }
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done // Enables "Done" action on keyboard
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus() // Dismisses the keyboard
                    viewModel.loginUser {
                        val updatedUserName = viewModel.userName.value
                        val updatedImageUrl = viewModel.userImageUrl.value

                        if (updatedUserName.isNotBlank()) {
                            val encodedUserName = URLEncoder.encode(updatedUserName, "UTF-8")
                            val encodedImageUrl = URLEncoder.encode(updatedImageUrl, "UTF-8")
                            navController.navigate("dashboard/$encodedUserName/$encodedImageUrl")
                        }
                    }
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = {
                viewModel.loginUser {
                    val updatedUserName = viewModel.userName.value
                    val updatedImageUrl = viewModel.userImageUrl.value

                    if (updatedUserName.isNotBlank()) {
                        val encodedUserName = URLEncoder.encode(updatedUserName, "UTF-8")
                        val encodedImageUrl = URLEncoder.encode(updatedImageUrl, "UTF-8")
                        navController.navigate("dashboard/$encodedUserName/$encodedImageUrl")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDB833))
        ) {
            if (loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = "Login",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "or use Biometrics",
            fontSize = 14.sp,
            color = Color.Gray,
            fontFamily = poppinsFontFamily,
            modifier = Modifier.padding(top = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        IconButton(
            onClick = {
                TODO("Implement biometric authentication logic here")
            },
            modifier = Modifier
                .size(72.dp)
                .background(Color(0xFFEFEFEF), shape = CircleShape)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_fingerprint),
                contentDescription = "Biometric Login",
                tint = Color.Gray,
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Sign in to different domain",
            fontFamily = poppinsFontFamily,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.clickable { /* Handle domain sign-in */ }
        )

        Spacer(modifier = Modifier.height(80.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(text = "Powered by", fontSize = 14.sp, color = Color.Gray,fontFamily = poppinsFontFamily)
        }
    }
}

//
fun getAuthToken(context: Context): String? {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("auth_token", null)
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val fakeNavController = rememberNavController()

    Surface {
        LoginScreen(navController = fakeNavController)
    }
}