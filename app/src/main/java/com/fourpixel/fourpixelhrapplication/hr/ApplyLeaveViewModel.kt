package com.fourpixel.fourpixelhrapplication.hr

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fourpixel.fourpixelhrapplication.client.ApiService
import com.fourpixel.fourpixelhrapplication.client.ApplyLeaveRequest
import com.fourpixel.fourpixelhrapplication.client.TypeForLeave
import com.fourpixel.fourpixelhrapplication.client.UserForLeave
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.UUID

// Define a sealed class to represent the events (success/failure)
sealed class ApplyLeaveEvent {
    data class Success(val message: String) : ApplyLeaveEvent()
    data class Error(val message: String) : ApplyLeaveEvent()
}

class ApplyLeavesViewModel(private val apiService: ApiService) : ViewModel() {

    var leaveDate by mutableStateOf("Leave Date")
        private set

    var reason by mutableStateOf("")
        private set

    var selectedLeaveType by mutableStateOf("")
        private set

    var selectedLeaveTypeId by mutableStateOf(-1)
        private set

    var selectedDuration by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    private val _applyLeaveEvent = MutableSharedFlow<ApplyLeaveEvent>()
    val applyLeaveEvent = _applyLeaveEvent.asSharedFlow()

    private val leaveTypeMapping = mapOf(
        "Annual Leave" to 1,
        "Casual Leave" to 2,
        "Medical Leave" to 3
    )

    fun onLeaveDateSelected(day: Int, month: Int, year: Int) {
        leaveDate = "${day}/${month + 1}/$year"
    }

    fun onReasonChanged(newReason: String) {
        reason = newReason
    }

    fun onLeaveTypeSelected(type: String) {
        selectedLeaveType = type
        selectedLeaveTypeId = leaveTypeMapping[type] ?: -1
    }

    // New function to handle duration selection
    fun onDurationSelected(duration: String) {
        selectedDuration = duration
    }

    fun applyLeave(token: String, userId: Int) {
        // Basic validation
        if (leaveDate == "Leave Date" || reason.isBlank() || selectedLeaveTypeId == -1 || selectedDuration.isBlank()) {
            viewModelScope.launch {
                _applyLeaveEvent.emit(ApplyLeaveEvent.Error("Please fill all required fields."))
            }
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                val uniqueId = UUID.randomUUID().toString()

                // Determine the 'duration' and 'halfDayType' based on selectedDuration
                val durationForApi: String
                val halfDayTypeForApi: String?

                when (selectedDuration) {
                    "Full Day" -> {
                        durationForApi = "full"
                        halfDayTypeForApi = null
                    }
                    "First Half" -> {
                        durationForApi = "half"
                        halfDayTypeForApi = "first_half"
                    }
                    "Second Half" -> {
                        durationForApi = "half"
                        halfDayTypeForApi = "second_half"
                    }
                    else -> {
                        // Default or error case if an unexpected duration is selected
                        durationForApi = "full"
                        halfDayTypeForApi = null
                    }
                }

                val status = "pending"

                val request = ApplyLeaveRequest(
                    uniqueId = uniqueId,
                    duration = durationForApi,
                    leaveDate = leaveDate,
                    reason = reason,
                    status = status,
                    halfDayType = halfDayTypeForApi,
                    user = UserForLeave(id = userId),
                    type = TypeForLeave(id = selectedLeaveTypeId)
                )

                val response = apiService.applyLeave("Bearer $token", request)

                if (response.isSuccessful) {
                    val message = response.body()?.message ?: "Leave applied successfully!"
                    _applyLeaveEvent.emit(ApplyLeaveEvent.Success(message))

                    clearForm()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = "Error: ${response.code()} - ${errorBody ?: "Unknown error"}"
                    _applyLeaveEvent.emit(ApplyLeaveEvent.Error(errorMessage))
                }
            } catch (e: IOException) {
                _applyLeaveEvent.emit(ApplyLeaveEvent.Error("Network error: ${e.message}"))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = "API error: ${e.code()} - ${errorBody ?: "Unknown API error"}"
                _applyLeaveEvent.emit(ApplyLeaveEvent.Error(errorMessage))
            } catch (e: Exception) {
                _applyLeaveEvent.emit(ApplyLeaveEvent.Error("An unexpected error occurred: ${e.message}"))
            } finally {
                isLoading = false
            }
        }
    }

    private fun clearForm() {
        leaveDate = "Leave Date"
        reason = ""
        selectedLeaveType = ""
        selectedLeaveTypeId = -1
        selectedDuration = "" // Clear selected duration as well
    }
}