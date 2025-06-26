package com.fourpixel.fourpixelhrapplication.features.noticeboard // Or your preferred package

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fourpixel.fourpixelhrapplication.client.ApiService
import com.fourpixel.fourpixelhrapplication.client.Notice
import com.fourpixel.fourpixelhrapplication.client.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoticeViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)

    private val apiService: ApiService = RetrofitClient.instance.create(ApiService::class.java)

    // Holds the list of notices
    private val _notices = MutableStateFlow<List<Notice>>(emptyList())
    val notices: StateFlow<List<Notice>> = _notices.asStateFlow()

    // Tracks the loading state (e.g., for showing a progress bar)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Holds any potential error messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        // Fetch notices when the ViewModel is first created
        fetchNotices()
    }

    /**
     * Fetches notices from the API and updates the UI state.
     */
    fun fetchNotices() {
        val token = sharedPreferences.getString("auth_token", null)
        if (token == null) {
            _errorMessage.value = "Authentication error. Please log in again."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = apiService.getNotices("Bearer $token")
                if (response.isSuccessful) {
                    _notices.value = response.body()?.data ?: emptyList()
                } else {
                    _errorMessage.value = "Error: Failed to load notices (Code: ${response.code()})"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: Please check your connection."
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}