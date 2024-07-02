package uz.fergana.it_center.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.fergana.it_center.model.CategoryModel
import uz.fergana.it_center.model.CourceModel


@Dao
interface CourceDao {
    @Query("DELETE from cource")
    fun deleteAllCource()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<CourceModel>)

    @Query("select * from cource")
    fun getAllCource(): List<CourceModel>
}