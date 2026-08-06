package com.example.financesmanagementapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.financesmanagementapp.data.local.dao.RecordsDAO
import com.example.financesmanagementapp.data.local.entities.RecordEntity
import com.example.financesmanagementapp.domain.model.Category


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
    version = 3,
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

        /**
         * Replaces the single `categoryName` column with `category`/`subcategory`, matching the
         * new two-level category taxonomy. Existing records can't be mapped to the new taxonomy
         * automatically, so they're simply reassigned to [Category.WITHOUT_CATEGORY].
         */
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE records_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        accountId INTEGER NOT NULL DEFAULT 0,
                        amount REAL NOT NULL,
                        description TEXT NOT NULL,
                        isIncome INTEGER NOT NULL,
                        category TEXT NOT NULL,
                        subcategory TEXT NOT NULL,
                        date TEXT NOT NULL,
                        currency TEXT NOT NULL
                    )
                """.trimIndent())
                db.execSQL("""
                    INSERT INTO records_new (id, accountId, amount, description, isIncome, category, subcategory, date, currency)
                    SELECT id, accountId, amount, description, isIncome, '${Category.WITHOUT_CATEGORY}', '', date, currency FROM records
                """.trimIndent())
                db.execSQL("DROP TABLE records")
                db.execSQL("ALTER TABLE records_new RENAME TO records")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    abstract fun recordsDao(): RecordsDAO
}
