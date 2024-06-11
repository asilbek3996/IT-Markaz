package com.example.itcenter.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.itcenter.model.AllStudentModel

@Dao
interface StudentsDao {
    @Query("DELETE from students")
    fun deleteAll()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllStudent(items: List<AllStudentModel>)

    @Query("select * from students")
    fun getAllStudents(): List<AllStudentModel>
}