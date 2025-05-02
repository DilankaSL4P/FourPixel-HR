package com.fourpixel.fourpixelhrapplication.Work

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fourpixel.fourpixelhrapplication.client.RetrofitClient
import com.fourpixel.fourpixelhrapplication.client.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)


    private val _allTasks = MutableStateFlow<List<Task>>(emptyList())
    val allTasks: StateFlow<List<Task>> = _allTasks

    private val _filteredTasks = MutableStateFlow<List<Task>>(emptyList())
    val filteredTasks: StateFlow<List<Task>> = _filteredTasks

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter

    init {
        fetchTasks()
    }

    private fun fetchTasks() {
        viewModelScope.launch {
            try {
                val token = sharedPreferences.getString("auth_token", null) ?: return@launch
                val bearerToken = "Bearer $token"
                val response = RetrofitClient.api.getMyTasks(bearerToken)
                if (response.isSuccessful) {
                    _allTasks.value = response.body()?.data ?: emptyList()
                    updateFilteredTasks()
                } else {
                    // Handle error (e.g., log or show UI message)
                }
            } catch (e: Exception) {
                // Handle network or unexpected errors
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        updateFilteredTasks()
    }

    fun updateFilter(filter: String) {
        _selectedFilter.value = filter
        updateFilteredTasks()
    }

    private fun updateFilteredTasks() {
        val statusMapping = mapOf(
            "Ongoing" to listOf("incomplete"),
            "Pending" to listOf("on hold"),
            "Completed" to listOf("completed"),
            "All" to null
        )

        val selected = _selectedFilter.value
        val statusesToMatch = statusMapping[selected]

        val filtered = _allTasks.value.filter { task ->
            val matchesStatus = statusesToMatch == null || statusesToMatch.contains(task.status.lowercase())
            val matchesSearch = task.heading.contains(_searchQuery.value, ignoreCase = true)
            matchesStatus && matchesSearch
        }

        _filteredTasks.value = filtered
    }
}