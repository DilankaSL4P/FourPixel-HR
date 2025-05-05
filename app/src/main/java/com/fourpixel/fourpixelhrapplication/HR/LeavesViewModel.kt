package com.fourpixel.fourpixelhrapplication.HR

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fourpixel.fourpixelhrapplication.client.Leave
import com.fourpixel.fourpixelhrapplication.client.RetrofitClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class LeavesViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)

    private val _allLeaves = MutableStateFlow<List<Leave>>(emptyList())
    private val _filteredLeaves = MutableStateFlow<Map<String, List<Leave>>>(emptyMap())
    val filteredLeaves: StateFlow<Map<String, List<Leave>>> = _filteredLeaves

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchLeaves()
    }

    fun fetchLeaves() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null

                val token = sharedPreferences.getString("auth_token", null)
                if (token == null) {
                    _errorMessage.value = "Not authenticated. Please log in."
                    return@launch
                }

                val bearerToken = "Bearer $token"
                val response = RetrofitClient.api.getLeaves(bearerToken)

                if (response.isSuccessful) {
                    val leaves = response.body()?.data ?: emptyList()
                    _allLeaves.value = leaves
                    updateFilteredLeaves()
                } else {
                    _errorMessage.value = "Failed to fetch leaves: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.localizedMessage ?: "Unknown error"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        updateFilteredLeaves()
    }

    //Backend is not mapped. filters are not working. check HERE!!!!!!!!!
    fun updateFilter(filter: String) {
        _selectedFilter.value = filter
        updateFilteredLeaves()
    }

    private fun updateFilteredLeaves() {
        val filtered = _allLeaves.value.filter { leave ->
            val matchesFilter = _selectedFilter.value == "All" ||
                    leave.leaveTypeId.toString() == _selectedFilter.value
            val matchesSearch = leave.reason.contains(_searchQuery.value, ignoreCase = true)
            matchesFilter && matchesSearch
        }.groupBy { it.leaveTypeId.toString() }

        _filteredLeaves.value = filtered
    }

    fun getCount(type: String): Int {
        return _filteredLeaves.value.values.flatten().count {
            type == "All" || it.leaveTypeId.toString() == type
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // ✅ NEW: Group filtered leaves by Month-Year
    val filteredLeavesByMonth: StateFlow<Map<String, List<Leave>>> = combine(
        _searchQuery, _selectedFilter, _allLeaves
    ) { query, filter, allLeaves ->
        val sdfInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdfOutput = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

        allLeaves
            .filter {
                val matchesFilter = filter == "All" || it.leaveTypeId.toString() == filter
                val matchesSearch = query.isBlank() || it.reason.contains(query, ignoreCase = true)
                matchesFilter && matchesSearch
            }
            .sortedByDescending { it.leaveDate }
            .groupBy { leave ->
                try {
                    val date = sdfInput.parse(leave.leaveDate)
                    date?.let { sdfOutput.format(it) } ?: "Unknown"
                } catch (e: Exception) {
                    "Unknown"
                }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())
}
