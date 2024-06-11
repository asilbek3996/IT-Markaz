package com.example.itcenter.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.itcenter.model.AllStudentModel
import com.example.itcenter.model.CategoryModel


@Database(entities = [AllStudentModel::class, CategoryModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getStudentDao(): StudentsDao
    abstract fun getCategoryDao(): CategoryDao
    companion object {
        var INSTANCE: AppDatabase? = null
        fun initDatabase(context: Context) {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java,"it_center").build()
                }
            }
        }

        fun getDatabase(): AppDatabase {
            return INSTANCE!!
        }
    }
}