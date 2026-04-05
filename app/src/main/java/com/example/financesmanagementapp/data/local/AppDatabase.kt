package com.example.financesmanagementapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.financesmanagementapp.data.local.dao.RecordsDAO
import com.example.financesmanagementapp.data.local.entities.RecordEntity


/**
 * The main class of the application database.
 *
 * Noted with @Database to define:
 * - entities: List of all tables (@Entity classes) that will be contained by this database.
 * - version: Database version. Must be incremented each time this db is modified.
 * - exportSchema: Recommended to be false because generates a build warning if you don't plan to export the schema.
 */
@Database(
    entities = [
        RecordEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val DATABASE_NAME = "financesAppDataBase"
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

    abstract fun recordsDao(): RecordsDAO
}