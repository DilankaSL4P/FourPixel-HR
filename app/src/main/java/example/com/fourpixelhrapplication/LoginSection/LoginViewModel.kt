package example.com.fourpixelhrapplication.LoginSection

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import example.com.fourpixelhrapplication.client.ApiService
import example.com.fourpixelhrapplication.client.LoginRequest
import example.com.fourpixelhrapplication.client.LoginResponse
import example.com.fourpixelhrapplication.client.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    private val apiService: ApiService = RetrofitClient.instance.create(ApiService::class.java)

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
            apiService.loginUser(LoginRequest(currentEmail, currentPassword)).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    _loading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        val loginResponse = response.body()!!
                        saveAuthToken(loginResponse.token)
                        onSuccess()
                    } else {
                        _loginError.value = "Invalid email or password."
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _loading.value = false
                    _loginError.value = "Login failed: ${t.message}"
                }
            })
        }
    }

    private fun saveAuthToken(token: String) {
        val sharedPreferences: SharedPreferences =
            getApplication<Application>().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("auth_token", token)
            apply()
        }
    }
}