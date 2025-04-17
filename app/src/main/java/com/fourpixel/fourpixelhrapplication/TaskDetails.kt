package com.fourpixel.fourpixelhrapplication

class TaskDetails (
    val id: String,
    val title: String,
    val category: String,
    val daysLeft: String,
    val status: String)

enum class TaskStatus{
    ONGOING, PENDING, COMPLETED
}