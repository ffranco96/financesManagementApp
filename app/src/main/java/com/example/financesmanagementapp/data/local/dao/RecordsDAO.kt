package com.example.financesmanagementapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.financesmanagementapp.data.local.entities.RecordEntity
import com.example.financesmanagementapp.domain.model.Record.Companion.DEFAULT_ACCOUNT_ID
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) interface for managing financial records in the database.
 */
@Dao
interface RecordsDAO {
    @Query("SELECT * FROM records")
    fun getAll(): List<RecordEntity>

    @Query("SELECT * FROM records")
    fun getAllAsFlow(): Flow<List<RecordEntity>>

    @Query("""
        SELECT 
            SUM(amount)
        FROM records 
        WHERE accountId = :accId
    """)
    fun getTotalBalanceByAccount(accId: Int = DEFAULT_ACCOUNT_ID): Flow<Double?>

    @Query("""
        SELECT 
            SUM(amount) 
        FROM records 
        WHERE accountId = :accId
        AND categoryName = :categoryName
    """)
    fun getBalanceByCategoryAndAccount(accId: Int = DEFAULT_ACCOUNT_ID, categoryName: String): Flow<Double?>

    @Query("DELETE FROM records")
    fun deleteAll()

    @Insert
    fun insert(vararg rec: RecordEntity)

    @Delete
    fun delete(rec: RecordEntity)

    @Update
    fun update(rec: RecordEntity)
}