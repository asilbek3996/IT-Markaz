package com.example.itcenter.model

data class Notification(
    val id: Int,
    val title: String?,
    val comment: String?,
    val image: String?,
    var createdAt: String,
    var isRead: Boolean = false
)