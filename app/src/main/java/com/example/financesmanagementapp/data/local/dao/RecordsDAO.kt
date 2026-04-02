package com.example.financesmanagementapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.financesmanagementapp.data.local.entities.RecordEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) interface for managing financial records in the database.
 */
@Dao
interface RecordsDAO {
    @Query("SELECT * FROM RECORDS")
    fun getAll(): List<RecordEntity>

    @Query("SELECT * FROM RECORDS")
    fun getAllAsFlow(): Flow<List<RecordEntity>>

    @Query("DELETE FROM RECORDS")
    fun deleteAll()

    @Insert
    fun insert(vararg rec: RecordEntity)

    @Delete
    fun delete(rec: RecordEntity)

    @Update
    fun update(rec: RecordEntity)
}