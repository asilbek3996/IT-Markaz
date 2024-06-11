package com.example.itcenter.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryModel(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    var id: Int,
    var image: String,
    var levelImage: String,
    var language: String,
    var createdAt: String,
    var updatedAt: String
)