package uz.fergana.it_center.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cource")
data class CourceModel(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    var id: Int? = null,
    var image: String? = null,
    var levelImage: String? = null,
    var language: String? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null
)