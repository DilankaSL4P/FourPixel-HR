package com.fourpixel.fourpixelhrapplication.LoginSection

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fourpixel.fourpixelhrapplication.client.ApiService
import com.fourpixel.fourpixelhrapplication.client.LoginRequest
import com.fourpixel.fourpixelhrapplication.client.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _showPassword = MutableStateFlow(false)
    val showPassword: StateFlow<Boolean> = _showPassword

    private val _loginError = MutableStateFlow("")
    val loginError: StateFlow<String> = _loginError

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _userImageUrl = MutableStateFlow("")
    val userImageUrl: StateFlow<String> = _userImageUrl

    private val apiService: ApiService = RetrofitClient.instance.create(ApiService::class.java)
    private val sharedPreferences: SharedPreferences =
        getApplication<Application>().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)

    init {
        _userName.value = sharedPreferences.getString("user_name", "") ?: ""
    }

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun toggleShowPassword() {
        _showPassword.value = !_showPassword.value
    }

    fun loginUser(onSuccess: () -> Unit) {
        val currentEmail = _email.value
        val currentPassword = _password.value


        if (currentEmail.isBlank() || currentPassword.isBlank()) {
            _loginError.value = "Please enter both email and password."
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentEmail).matches()) {
            _loginError.value = "Invalid email format."
            return
        }

        _loading.value = true
        viewModelScope.launch {
            try {
                println("DEBUG: Attempting login with Email: $currentEmail") // Removed password from logs

                val response = apiService.loginUser(LoginRequest(currentEmail, currentPassword))
                println("DEBUG: Raw Response - ${response.raw()}")

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    println("DEBUG: Response Body - $loginResponse")

                    if (loginResponse != null) {
                        saveAuthToken(loginResponse.data.token)
                        saveUserName(loginResponse.data.user.name)
                        saveUserImageUrl(loginResponse.data.user.imageUrl ?: "")
                        onSuccess()
                    } else {
                        _loginError.value = "Empty response from server"
                        println("DEBUG: Empty response body")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    println("DEBUG: API Error - Code: ${response.code()}, Message: $errorBody")

                    _loginError.value = when (response.code()) {
                        401 -> "Invalid credentials"
                        403 -> "Access denied"
                        404 -> "Service not found"
                        500 -> "Server error"
                        else -> "Login failed (Error ${response.code()})"
                    }
                }
            } catch (e: IOException) {
                _loginError.value = "Network error. Please check your connection."
                println("DEBUG: IOException - ${e.localizedMessage}")
            } catch (e: HttpException) {
                _loginError.value = "HTTP error: ${e.message}"
                println("DEBUG: HttpException - ${e.localizedMessage}")
            } catch (e: Exception) {
                _loginError.value = "Unexpected error: ${e.localizedMessage}"
                println("DEBUG: Unexpected Exception - ${e.localizedMessage}")
            } finally {
                _loading.value = false
            }
        }
    }


    private fun saveAuthToken(token: String) {
        with(sharedPreferences.edit()) {
            putString("auth_token", token)
            apply()
        }
    }

    private fun saveUserName(name: String?) {
        val safeName = name ?: "DefaultUser" // Ensures it's never null
        with(sharedPreferences.edit()) {
            putString("user_name", safeName)
            apply()
        }
        _userName.value = safeName
    }

    private fun saveUserImageUrl(imageUrl: String) {
        with(sharedPreferences.edit()) {
            putString("user_image_url", imageUrl)
            apply()
        }
        _userImageUrl.value = imageUrl
    }


}