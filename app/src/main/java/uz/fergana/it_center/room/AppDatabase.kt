package uz.fergana.it_center.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.fergana.it_center.model.AllStudentModel
import uz.fergana.it_center.model.CategoryModel
import uz.fergana.it_center.model.CourceModel


@Database(entities = [AllStudentModel::class, CategoryModel::class, CourceModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getStudentDao(): StudentsDao
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getCourceDao(): CourceDao
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