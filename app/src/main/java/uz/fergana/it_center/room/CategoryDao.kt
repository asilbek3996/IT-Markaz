package uz.fergana.it_center.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.fergana.it_center.model.CategoryModel


@Dao
interface CategoryDao {
    @Query("DELETE from category")
    fun deleteAllCategory()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<CategoryModel>)

    @Query("select * from category")
    fun getAllCategory(): List<CategoryModel>
}