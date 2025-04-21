package com.fourpixel.fourpixelhrapplication.HR

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class LeavesViewModel : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter



    private val _leaves = MutableStateFlow(LeaveList()) // Simulated data
    val filteredLeaves: StateFlow<Map<String, List<Leave>>> = combine(_searchQuery, _selectedFilter, _leaves) { query, filter, leaves ->
        val filtered = leaves.filter {
            (filter == "All" || it.type == filter) &&
                    (query.isBlank() || it.date.contains(query, ignoreCase = true))
        }
        filtered.groupBy { it.monthYear }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateFilter(filter: String) {
        _selectedFilter.value = filter
    }

    private fun LeaveList(): List<Leave> = listOf(
        Leave("1 Day Leave", "Wed, 15th Oct", "Medical", "Approved", "October 2024"),
        Leave("Half Day Leave", "Wed, 08th Oct", "Casual", "Rejected", "October 2024"),
        Leave("1 Day Leave", "Mon, 02nd Aug - Thu, 06th Aug", "Medical", "Approved", "August 2024"),
        Leave("1 Day Leave", "Fri, 26th Aug", "Annual", "Approved", "August 2024"),
    )

    fun getCount(type: String): Int {
        val leaves = _leaves.value
        return if (type == "All") {
            leaves.size
        } else {
            leaves.count { it.type == type }
        }
    }



}
