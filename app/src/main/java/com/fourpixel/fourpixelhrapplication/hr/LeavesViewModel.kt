package com.fourpixel.fourpixelhrapplication.hr

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
import com.fourpixel.fourpixelhrapplication.client.ApiService

class LeavesViewModel(application: Application) : AndroidViewModel(application) {

    // Mapping to convert UI filter names to backend leaveTypeId values
    private val leaveTypeMap = mapOf(
        "All" to "All",
        "Casual" to "1",
        "Medical" to "2",
        "Annual" to "3"
    )

    // Reversed Mapping for getting UI names from ID values
    private val leaveTypeNameMap = mapOf(
        "1" to "Casual",
        "2" to "Medical",
        "3" to "Annual"
    )

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)

    private val apiService: ApiService = RetrofitClient.instance.create(ApiService::class.java)

    private val _allLeaves = MutableStateFlow<List<Leave>>(emptyList())
    val allLeaves: StateFlow<List<Leave>> = _allLeaves

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
                val response = apiService.getLeaves(bearerToken)

                if (response.isSuccessful) {
                    val leaves = response.body()?.data ?: emptyList()
                    _allLeaves.value = leaves
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
    }

    fun updateFilter(filter: String) {
        _selectedFilter.value = filter
    }

    // Filtered leaves by type and search query
    val filteredLeaves: StateFlow<List<Leave>> = combine(
        _searchQuery, _selectedFilter, _allLeaves
    ) { query, filter, allLeaves ->
        allLeaves.filter { leave ->
            val filterTypeId = leaveTypeMap[filter] ?: "All"
            val matchesFilter = filterTypeId == "All" || leave.leaveTypeId.toString() == filterTypeId
            val matchesSearch = query.isBlank() || leave.reason.contains(query, ignoreCase = true)
            matchesFilter && matchesSearch
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Group filtered leaves by Month-Year
    val filteredLeavesByMonth: StateFlow<Map<String, List<Leave>>> = filteredLeaves
        .map { leaves ->
            val sdfInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val sdfOutput = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

            leaves
                .sortedByDescending { it.leaveDate }
                .groupBy { leave ->
                    try {
                        val date = sdfInput.parse(leave.leaveDate)
                        date?.let { sdfOutput.format(it) } ?: "Unknown"
                    } catch (e: Exception) {
                        "Unknown"
                    }
                }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    // Get count of leaves for a specific filter type
    fun getCount(filterType: String): Int {
        val filterTypeId = leaveTypeMap[filterType] ?: "All"
        return _allLeaves.value.count {
            filterTypeId == "All" || it.leaveTypeId.toString() == filterTypeId
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // Helper function to get leave type name from ID
    fun getLeaveTypeName(leaveTypeId: Int): String {
        return leaveTypeNameMap[leaveTypeId.toString()] ?: "Unknown"
    }
}