package com.fourpixel.fourpixelhrapplication.DashBoardSection

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.fourpixel.fourpixelhrapplication.client.ApiService
import com.fourpixel.fourpixelhrapplication.client.ClockInRequest
import com.fourpixel.fourpixelhrapplication.client.Notice
import com.fourpixel.fourpixelhrapplication.client.RetrofitClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Job
import kotlinx.coroutines.tasks.await
import android.Manifest
import android.location.Location
import com.fourpixel.fourpixelhrapplication.client.ClockOutRequest
import com.fourpixel.fourpixelhrapplication.client.TodayAttendanceRootData
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.common.api.ResolvableApiException
import java.net.URLDecoder


class DashboardViewModelJP(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)

    private val _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()

    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime = _elapsedTime.asStateFlow()

    private val _selectedOption = MutableStateFlow("Working From")
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

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private val _projectCount = MutableStateFlow(0)
    val projectCount = _projectCount.asStateFlow()

    private val _todayAttendance = MutableStateFlow<TodayAttendanceRootData?>(null)
    val todayAttendance: StateFlow<TodayAttendanceRootData?> = _todayAttendance.asStateFlow()

    //Location setting up
    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0)
        .setDurationMillis(3000)
        .setMaxUpdates(1)
        .build()

    private val locationSettingsRequest = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)
        .setAlwaysShow(true)
        .build()

    private val _resolveLocationSettingsEvent = MutableSharedFlow<ResolvableApiException>()
    val resolveLocationSettingsEvent = _resolveLocationSettingsEvent.asSharedFlow()

    private val _showToastEvent = MutableSharedFlow<String>()
    val showToastEvent = _showToastEvent.asSharedFlow()

    private var timerJob: Job? = null

    init {
        loadUserName()
        viewModelScope.launch{
            launch { fetchProjectCount() }
            launch { fetchTaskCount() }
        }
        fetchNotices()
    }

    fun setUserName(name: String) {
        val decodedName = URLDecoder.decode(name, "UTF-8")
        val firstName = decodedName.split(" ")[0]
        _userName.value = firstName
        saveUserName(firstName)
    }

    private fun saveUserName(name: String) {
        with(sharedPreferences.edit()) {
            putString("user_name", name)
            apply()
        }
    }

    private fun loadUserName() {
        val savedName = sharedPreferences.getString("user_name", "") ?: ""
        _userName.value = savedName.ifBlank { "User" }
    }

    fun toggleClockIn() {

        if (!_isRunning.value) {
            _isRunning.value = true
            _elapsedTime.value = 0L
            startTimer()

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
    }

    fun setSelectedOption(option: String) {
        _selectedOption.value = option
    }

    /*private fun updateStatus(assigned: Int, pending: Int) {
        _assignedProjects.value = assigned
        _pendingTasks.value = pending
    }*/

    private fun startTimer() {
        // Cancel existing timer to prevent multiple timers running
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            while (_isRunning.value) {
                delay(1000L)
                _elapsedTime.value += 1
            }

        }
    }

    private fun fetchProjectCount() {
        val token = sharedPreferences.getString("auth_token", null) ?: return

        viewModelScope.launch {
            try {
                val response = apiService.getProjects("Bearer $token")
                if (response.isSuccessful) {
                    val total = response.body()?.meta?.paging?.total ?: 0
                    _projectCount.value = total
                    _assignedProjects.value = total

                } else {
                    println("DEBUG: Error fetching projects - ${response.code()}")
                }
            } catch (e: Exception) {
                println("DEBUG: Exception while fetching project count - ${e.localizedMessage}")
            }
        }
    }

    private fun fetchTaskCount() {
        val token = sharedPreferences.getString("auth_token", null) ?: return

        viewModelScope.launch {
            try {
                val response = apiService.getMyTasks("Bearer $token")
                if (response.isSuccessful) {
                    val totalTasks = response.body()?.meta?.paging?.total ?: 0
                    _pendingTasks.value = totalTasks

                } else {
                    println("DEBUG: Error fetching tasks - ${response.code()}")
                }
            } catch (e: Exception) {
                println("DEBUG: Exception while fetching task count - ${e.localizedMessage}")
            }
        }
    }

    private fun fetchNotices() {
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

    fun fetchTodayAttendance() {
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

    private suspend fun getCurrentLocation(): Location? {

        if (!checkLocationPermission(getApplication())) {
            showToast("Location permission is required. Please grant it in settings.")
            return null
        }


        return try {
            val location = fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
            if (location == null) {
                showToast("Failed to get your current location. Please try again.")
            }
            location
        } catch (e: Exception) {

            if (e is ResolvableApiException) {
                _resolveLocationSettingsEvent.emit(e)
                showToast("Please enable location services.")
            } else {
                showToast("Could not retrieve location: ${e.message}")
                println("Error getting location: ${e.localizedMessage}")
            }
            null // Return null on any exception
        }
    }



    fun handleClockInButtonClick(settingsClient: SettingsClient,
                                 fusedLocationClient: FusedLocationProviderClient,
                                 context: Context)
    {
        // Check whether clocked in already
        if (_isRunning.value) {
            println("DEBUG: Already clocked in. Button press ignored.")
            showToast("You are already clocked in.")
            return
        }

        val token = sharedPreferences.getString("auth_token", null)
        if (token == null) {
            println("DEBUG: Auth token not found. Cannot clock in.")
            showToast("Authentication error. Please log in again.")
            revertClockInState()
            return
        }

        // Check Location Permission BEFORE attempting to get location
        if (!checkLocationPermission(getApplication())) {
            println("DEBUG: Location permission not granted. Requesting permission...")
            showToast("Location permission is required for clock-in. Please grant it.")
            revertClockInState()
            return
        }

       //Get location
        viewModelScope.launch {
            var currentLatitude: Double? = null
            var currentLongitude: Double? = null

            try {


                /*val locationSettingsResponse = settingsClient.checkLocationSettings(locationSettingsRequest).await()
                println("DEBUG: Location settings satisfied.")*/

                // Request current location
                val location: Location? = fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()

                if (location != null) {
                    currentLatitude = location.latitude
                    currentLongitude = location.longitude
                    println("Location- Lat=${currentLatitude}, Long=${currentLongitude}")

                    // Proceed with API call now that location is obtained
                    performClockInApiCall(token, currentLatitude, currentLongitude)

                } else {
                    println("Cannot get current location. Location object is null.")
                    showToast("Failed to get your current location. Please try again.")
                    revertClockInState()
                }

            } catch (e: Exception) {
                if (e is ResolvableApiException) {
                    // Location settings are not satisfied, but can be fixed by user.
                    println("DEBUG: Location settings not satisfied, but resolvable. Prompting user...")
                    _resolveLocationSettingsEvent.emit(e) // Emit event for UI to resolve
                    showToast("Please enable location services to clock in.")
                } else {
                    // Other errors
                    println("Error getting location: ${e.localizedMessage}")
                    showToast("Failed to get your current location. Please ensure location services are enabled and try again.")
                }
                revertClockInState()
            }
        }
    }

    private suspend fun performClockInApiCall(token: String, latitude: Double?, longitude: Double?) {
        _isRunning.value = true
        _elapsedTime.value = 0L
        startTimer()

        val workingFrom = "Office"
        try {
            println("Clocking in with working_from: $workingFrom, Lat: $latitude, Long: $longitude")
            val workFromType = "office"
            val requestBody = ClockInRequest(workFromType, workingFrom, latitude, longitude)
            val response = apiService.clockIn("Bearer $token", requestBody)

            if (response.isSuccessful) {
                val clockInResponse = response.body()
                if (clockInResponse != null) {
                    if (clockInResponse.message == "Clocked in successfully") {
                        println("Clock-in successful on API. Message: ${clockInResponse.message}")
                        // *** CRUCIAL: Rely on fetchTodayAttendance to get the actual attendance ID ***
                        fetchTodayAttendance() // This call will correctly populate _todayAttendance.value
                        showToast("Clocked in successfully!") // User-friendly toast

                    } else {
                        val errorMessage = clockInResponse.message
                        println("API Clock-in succeeded with unexpected message - ${response.code()} Message: $errorMessage")
                        showToast("Clock-in response: $errorMessage")
                        revertClockInState()
                    }
                } else {
                    println("API Clock-in failed: Empty response body despite 200 OK.")
                    showToast("Clock-in failed: Server returned empty response.")
                    revertClockInState()
                }
            } else {
                val errorBody = response.errorBody()?.string()
                println("DEBUG: API Clock-in failed (HTTP error) - ${response.code()} Error: $errorBody")
                showToast("Clock-in failed: Server error ${response.code()}. Details: ${errorBody ?: "No details"}")
                revertClockInState()
            }
        } catch (e: Exception) {
            println("DEBUG: Clock-in API call exception - ${e.localizedMessage}")
            e.printStackTrace()
            showToast("Clock-in failed due to a network error. Please check your internet connection.")
            revertClockInState()
        }
    }

    private fun showToast(message: String) {
        println("TOAST: $message")
    }

    // Helper function to check location permissions
    private fun checkLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    // Helper function to revert UI state
    private fun revertClockInState() {
        _isRunning.value = false
        timerJob?.cancel()
        _elapsedTime.value = 0L
    }



    fun confirmClockOut() {
        val token = sharedPreferences.getString("auth_token", null)
        if (token == null) {
            viewModelScope.launch { _showToastEvent.emit("Authentication error. Please log in again.") }
            _showDialog.value = false
            return
        }
        val currentAttendanceRecord = _todayAttendance.value?.attendanceRecord
        if (currentAttendanceRecord == null || currentAttendanceRecord.id <= 0) {
            println("DEBUG: No valid active clock-in record found for clock-out. Aborting.")
            viewModelScope.launch { _showToastEvent.emit("You are not currently clocked in.") }
            fetchTodayAttendance()
            revertClockInState()
            _showDialog.value = false
            return
        }

        viewModelScope.launch {

            val location = getCurrentLocation()

            if (location == null) {
                _showDialog.value = false
                return@launch
            }

            val currentLat = location.latitude
            val currentLng = location.longitude

            val clockOutRequestBody = ClockOutRequest(
                currentLatitude = currentLat,
                currentLongitude = currentLng
            )

            val attendanceId = currentAttendanceRecord.id
            println("DEBUG: Sending clock-out request for attendance ID: $attendanceId with location (Lat: $currentLat, Lng: $currentLng)")

            try {
                val response = apiService.clockOut(
                    token = "Bearer $token",
                    attendanceId = attendanceId,
                    body = clockOutRequestBody
                )

                if (response.isSuccessful) {
                    val successMessage = response.body()?.message ?: "Clocked out successfully!"
                    println("DEBUG: Clock-out successful from server: $successMessage")
                    _showToastEvent.emit(successMessage)
                    revertClockInState()
                    _todayAttendance.value = null
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = "Clock-out failed: ${errorBody ?: "Server error ${response.code()}"}"
                    println("DEBUG: $errorMessage")
                    _showToastEvent.emit(errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = "Clock-out failed due to a network error."
                println("DEBUG: Clock-out API call exception - ${e.localizedMessage}")
                _showToastEvent.emit(errorMessage)
            } finally {
                // Always close the dialog
                _showDialog.value = false
            }
        }
    }

    // Override onCleared to cancel any running jobs when the ViewModel is destroyed
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}