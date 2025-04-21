package com.fourpixel.fourpixelhrapplication.Work


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TaskListViewModel : ViewModel() {

    private val allTasks = listOf(
        Task("P001-02", "Task Page Design", "Mobile App UI", "Ongoing", "5 Days left"),
        Task("P001-03", "Holiday Page Design", "Mobile App UI", "Pending", "14 Days left"),
        Task("P001-01", "Login Page Design", "Mobile App UI", "Completed", "Completed on 2024.11.10")
    )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter

    val filteredTasks: StateFlow<List<Task>> = MutableStateFlow(allTasks)

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        updateFilteredTasks()
    }

    fun updateFilter(filter: String) {
        _selectedFilter.value = filter
        updateFilteredTasks()
    }

    private fun updateFilteredTasks() {
        val filtered = when (_selectedFilter.value) {
            "All" -> allTasks
            else -> allTasks.filter { it.status == _selectedFilter.value }
        }.filter { it.title.contains(_searchQuery.value, ignoreCase = true) }

        (filteredTasks as MutableStateFlow).value = filtered
    }
}