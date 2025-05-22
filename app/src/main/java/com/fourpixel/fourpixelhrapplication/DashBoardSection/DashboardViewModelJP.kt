package com.fourpixel.fourpixelhrapplication.DashBoardSection

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.fourpixel.fourpixelhrapplication.client.ApiService
import com.fourpixel.fourpixelhrapplication.client.ClockInRequest
import com.fourpixel.fourpixelhrapplication.client.Notice
import com.fourpixel.fourpixelhrapplication.client.RetrofitClient
import com.fourpixel.fourpixelhrapplication.client.TodayAttendanceData
import kotlinx.coroutines.Job


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

    private val _todayAttendance = MutableStateFlow<TodayAttendanceData?>(null)
    val todayAttendance: StateFlow<TodayAttendanceData?> = _todayAttendance.asStateFlow()

    private var timerJob: Job? = null


    init {
        loadUserName()
        fetchProjectCount()
        fetchTaskCount()
        fetchNotices()
    }

    fun setUserName(name: String) {
        _userName.value = name
        saveUserName(name)
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
        // Only clock in if not already running
        if (!_isRunning.value) {
            _isRunning.value = true
            _elapsedTime.value = 0L // Reset elapsed time when starting a new session
            startTimer()
            // clockInToServer() is called directly from the UI onClick, not here.
            // This method just handles the timer state.
        }
    }

    fun toggleClockOut() {
        if (_isRunning.value) {
            _isRunning.value = false
            _elapsedTime.value = 0L
            _showDialog.value = true
        }
    }

    fun showClockOutDialog() {
        // Only show dialog if the timer is actually running
        if (_isRunning.value) {
            _showDialog.value = true
        }
    }

    fun dismissDialog() {
        _showDialog.value = false
        // Important: If dialog is dismissed, the clock should remain running.
        // No change to _isRunning.value here.
    }

    fun setSelectedOption(option: String) {
        _selectedOption.value = option
    }

    fun updateStatus(assigned: Int, pending: Int) {
        _assignedProjects.value = assigned
        _pendingTasks.value = pending
    }

    private fun startTimer() {
        // Cancel any existing timer job to prevent multiple timers running
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            // CollectLatest is good, but the while(running) loop inside it is more robust
            // to ensure continuous operation as long as _isRunning.value is true.
            // We'll use a direct check of _isRunning.value within the loop.
            while (_isRunning.value) { // Continue as long as _isRunning is true
                delay(1000L)
                _elapsedTime.value += 1
            }
            // Once _isRunning becomes false, the loop will exit, and the job will complete.
            // This handles the stopping of the timer cleanly.
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
                    updateStatus(
                        assigned = total,
                        pending = 0
                    ) // Optional: update assigned projects
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

    private fun fetchTodayAttendance() {
        viewModelScope.launch {
            try {

                val token = sharedPreferences.getString("auth_token", null) ?: return@launch
                val response = apiService.getTodayAttendance("Bearer $token")
                if (response.isSuccessful) {
                    _todayAttendance.value = response.body()?.data
                    println("DEBUG: Fetched today’s attendance - ${response.body()?.data}")
                } else {
                    println("DEBUG: Failed to fetch today's attendance - ${response.code()}")
                }
            } catch (e: Exception) {
                println("DEBUG: Error fetching today's attendance - ${e.localizedMessage}")
            }
        }
    }

    fun clockInToServer() {
        val token = sharedPreferences.getString("auth_token", null) ?: return
        val workingFrom = selectedOption.value

        viewModelScope.launch {
            try {
                val response = apiService.clockIn("Bearer $token", ClockInRequest(workingFrom))
                if (response.isSuccessful && response.body()?.success == true) {
                    println("DEBUG: Clock-in successful - ${response.body()?.message}")

                    // Now fetch today's attendance
                    fetchTodayAttendance()

                } else {
                    println(
                        "DEBUG: Clock-in failed - ${response.code()} ${
                            response.errorBody()?.string()
                        }"
                    )
                }
            } catch (e: Exception) {
                println("DEBUG: Clock-in exception - ${e.localizedMessage}")
            }
        }
    }

    fun confirmClockOut(reason: String) {
        val token = sharedPreferences.getString("auth_token", null) ?: return

        viewModelScope.launch {
            try {
                val requestBody = mapOf("reason" to reason)
                val response = apiService.clockOut("Bearer $token", requestBody)

                // --- START OF MODIFICATION ---
                // Always reset UI state for timer upon confirmation, regardless of API success.
                // This makes the UI responsive.
                _isRunning.value = false // Stop the timer
                timerJob?.cancel()       // Cancel the coroutine job
                _elapsedTime.value = 0L  // Reset elapsed time to 0
                _showDialog.value = false // Dismiss the dialog
                // --- END OF MODIFICATION ---

                if (response.isSuccessful && response.body() != null) {
                    println("DEBUG: Clock-out successful - ${response.body()?.message}")
                    fetchTodayAttendance() // Fetch updated attendance if successful
                } else {
                    println(
                        "DEBUG: Clock-out failed - ${response.code()} ${
                            response.errorBody()?.string()
                        }"
                    )
                    // TODO: You might want to show a Toast or Snackbar to the user
                    // indicating that clock-out on the server failed.
                }
            } catch (e: Exception) {
                println("DEBUG: Clock-out exception - ${e.localizedMessage}")
                // Ensure UI state is reset even if there's a network/other exception
                _isRunning.value = false
                timerJob?.cancel()
                _elapsedTime.value = 0L
                _showDialog.value = false
                // TODO: You might want to show a Toast or Snackbar to the user
                // indicating a network error.
            }
        }
    }

    // Override onCleared to cancel any running jobs when the ViewModel is destroyed
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}