package com.fourpixel.fourpixelhrapplication.DashBoardSection

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.fourpixel.fourpixelhrapplication.client.ApiService
import com.fourpixel.fourpixelhrapplication.client.Notice
import com.fourpixel.fourpixelhrapplication.client.RetrofitClient
import kotlinx.coroutines.launch


class DashboardViewModelJP(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)

    private val _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()

    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime = _elapsedTime.asStateFlow()

    private val _selectedOption = MutableStateFlow("Office")
    val selectedOption = _selectedOption.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private val _assignedProjects = MutableStateFlow(0)
    val assignedProjects = _assignedProjects.asStateFlow()

    private val _pendingTasks = MutableStateFlow(0)
    val pendingTasks = _pendingTasks.asStateFlow()

    private val _notices = MutableStateFlow<List<Notice>>(emptyList())
    val notices = _notices.asStateFlow()

    private val _showNoticePopup = MutableStateFlow(false)
    val showNoticePopup = _showNoticePopup.asStateFlow()

    private val apiService: ApiService = RetrofitClient.instance.create(ApiService::class.java)

    private val _projectCount = MutableStateFlow(0)
    val projectCount = _projectCount.asStateFlow()








    init {
        loadUserName()
        fetchProjectCount()
        fetchTaskCount()
        fetchNotices()
    }

    fun setUserName(name: String) {
        _userName.value = name
        saveUserName(name) // Save it persistently
    }

    private fun saveUserName(name: String) {
        with(sharedPreferences.edit()) {
            putString("user_name", name)
            apply()
        }
    }

    private fun loadUserName() {
        val savedName = sharedPreferences.getString("user_name", "") ?: ""
        _userName.value = if (savedName.isNotBlank()) savedName else "User"
    }

    fun toggleClockIn() {
        if (!_isRunning.value) {
            _isRunning.value = true
            _elapsedTime.value = 0L // Reset elapsed time when starting
            startTimer()
        } else {
            _isRunning.value = false
        }
    }

    fun toggleClockOut() {
        if (_isRunning.value) {
            _isRunning.value = false
            _elapsedTime.value = 0L // Reset elapsed time on clock-out
            _showDialog.value = true
        }
    }

    fun dismissDialog() {
        _showDialog.value = false
    }

    fun setSelectedOption(option: String) {
        _selectedOption.value = option
    }

    fun updateStatus(assigned: Int, pending: Int) {
        _assignedProjects.value = assigned
        _pendingTasks.value = pending
    }

    private fun startTimer() {
        viewModelScope.launch {
            isRunning.collectLatest { running ->
                if (running) {
                    while (running) {
                        delay(1000L)
                        _elapsedTime.value += 1
                    }
                }
            }
        }
    }

    fun fetchProjectCount() {
        val token = sharedPreferences.getString("auth_token", null) ?: return

        viewModelScope.launch {
            try {
                val response = apiService.getProjects("Bearer $token")
                if (response.isSuccessful) {
                    val total = response.body()?.meta?.paging?.total ?: 0
                    _projectCount.value = total
                    updateStatus(assigned = total, pending = 0) // Optional: update assigned projects
                } else {
                    println("DEBUG: Error fetching projects - ${response.code()}")
                }
            } catch (e: Exception) {
                println("DEBUG: Exception while fetching project count - ${e.localizedMessage}")
            }
        }
    }

    fun fetchTaskCount() {
        val token = sharedPreferences.getString("auth_token", null) ?: return

        viewModelScope.launch {
            try {
                val response = apiService.getMyTasks("Bearer $token")
                if (response.isSuccessful) {
                    val totalTasks = response.body()?.meta?.paging?.total ?: 0
                    _pendingTasks.value = totalTasks
                    updateStatus(assigned = _projectCount.value, pending = totalTasks)
                } else {
                    println("DEBUG: Error fetching tasks - ${response.code()}")
                }
            } catch (e: Exception) {
                println("DEBUG: Exception while fetching task count - ${e.localizedMessage}")
            }
        }
    }

    fun fetchNotices() {
        val token = sharedPreferences.getString("auth_token", null) ?: return

        viewModelScope.launch {
            try {
                val response = apiService.getNotices("Bearer $token")
                if (response.isSuccessful) {
                    val noticesList = response.body()?.data ?: emptyList()
                    _notices.value = noticesList

                    _showNoticePopup.value = noticesList.isNotEmpty()
                } else {
                    println("DEBUG: Error fetching notices - ${response.code()}")
                }
            } catch (e: Exception) {
                println("DEBUG: Exception while fetching notices - ${e.localizedMessage}")
            }
        }
    }


}
