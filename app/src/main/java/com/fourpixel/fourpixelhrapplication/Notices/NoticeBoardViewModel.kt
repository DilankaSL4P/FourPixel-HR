package com.fourpixel.fourpixelhrapplication.Notices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fourpixel.fourpixelhrapplication.client.Notice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoticeBoardViewModel : ViewModel() {

    private val _notices = MutableStateFlow<List<Notice>>(emptyList())
    val notices: StateFlow<List<Notice>> = _notices

    init {
        fetchNotices()
    }

    private fun fetchNotices() {
        viewModelScope.launch {
            // TODO: Replace this with your actual API call
            val noticesFromApi = listOf(
                Notice(1, "Maintenance Downtime", "System update at 5 PM today.", "All"),
                Notice(2, "Holiday Announcement", "Office closed on Diwali.", "Employee"),
                Notice(3, "Team Meeting", "Monthly team meeting at 3 PM.", "Manager"),
            )
            _notices.value = noticesFromApi
        }
    }
}