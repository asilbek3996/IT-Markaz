package com.example.itcenter.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.itcenter.model.AllStudentModel
import com.example.itcenter.model.CategoryModel


@Dao
interface CategoryDao {
    @Query("DELETE from category")
    fun deleteAllCategory()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<CategoryModel>)

    @Query("select * from category")
    fun getAllCategory(): List<CategoryModel>
}