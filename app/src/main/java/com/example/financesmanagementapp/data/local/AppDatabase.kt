package com.example.financesmanagementapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    entities = [RecordEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        const val DATABASE_NAME = "finances_app_database"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE records ADD COLUMN accountId INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    abstract fun recordsDao(): RecordsDAO
}
