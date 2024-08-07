package uz.fergana.it_center.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryModel(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    var id: Int? = null,
    var image: String? = null,
    var language: String? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null
)