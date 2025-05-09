package com.fourpixel.fourpixelhrapplication.Work

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fourpixel.fourpixelhrapplication.client.Project
import com.fourpixel.fourpixelhrapplication.client.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProjectsViewModel (application: Application) : AndroidViewModel(application) {


    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)

    private val _allProjects = MutableStateFlow<List<Project>>(emptyList())
    val allProjects: StateFlow<List<Project>> = _allProjects

    private val _filteredProjects = MutableStateFlow<List<Project>>(emptyList())
    val filteredProjects: StateFlow<List<Project>> = _filteredProjects

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter

    init {
        fetchProjects()
    }

    private fun fetchProjects() {
        viewModelScope.launch {
            try {
                val token = sharedPreferences.getString("auth_token", null) ?: return@launch
                val bearerToken = "Bearer $token"
                val response = RetrofitClient.api.getProjects(bearerToken)
                if (response.isSuccessful) {
                    _allProjects.value = response.body()?.data ?: emptyList()
                    updateFilteredProjects()
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        updateFilteredProjects()
    }

    fun updateFilter(filter: String) {
        _selectedFilter.value = filter
        updateFilteredProjects()
    }

    private fun updateFilteredProjects() {
        val statusMapping = mapOf(
            "Ongoing" to listOf("in progress"),
            "Pending" to listOf("on hold"),       //Check API Again!!!!!. Needs to be mapped. backend values are not received from API
            "Completed" to listOf("completed"),   //Check API Again!!!!
            "All" to null
        )

        val selected = _selectedFilter.value
        val statusesToMatch = statusMapping[selected]

        val filtered = _allProjects.value.filter { project ->
            val matchesStatus = statusesToMatch == null || statusesToMatch.contains(project.status.lowercase())
            val matchesSearch = project.projectName.contains(_searchQuery.value, ignoreCase = true)
            matchesStatus && matchesSearch
        }

        _filteredProjects.value = filtered
    }
}


//Filters are not working. and count is not there.!!!!!!!!