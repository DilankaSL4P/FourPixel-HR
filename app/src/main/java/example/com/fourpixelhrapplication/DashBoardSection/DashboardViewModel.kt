package example.com.fourpixelhrapplication.DashBoardSection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DashboardViewModel : ViewModel() {

    private val _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()

    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime = _elapsedTime.asStateFlow()


    private val _selectedOption = MutableStateFlow("Working from")
    val selectedOption = _selectedOption.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    fun toggleClockIn() {
        if (!_isRunning.value) {
            _isRunning.value = true
            startTimer()
        }
    }

    fun toggleClockOut() {
        _isRunning.value = false
        _elapsedTime.value = 0L
        _showDialog.value = true
    }

    fun dismissDialog() {
        _showDialog.value = false
    }

    fun setSelectedOption(option: String) {
        _selectedOption.value = option
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (_isRunning.value) {
                delay(1000L)
                _elapsedTime.value += 1
            }
        }
    }

}