package com.example.financesmanagementapp.di

import android.content.Context
import com.example.financesmanagementapp.data.local.CsvFileWriter
import com.example.financesmanagementapp.data.local.RecordsCsvFileWriter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides the [CsvFileWriter] binding.
 *
 * Installs in [SingletonComponent] so the writer is a process-wide singleton.
 */
@Module
@InstallIn(SingletonComponent::class)
object CsvModule {

    /**
     * Provides the [CsvFileWriter] implementation backed by Android file I/O.
     *
     * @param context Application context injected by Hilt.
     * @return A [RecordsCsvFileWriter] instance.
     */
    @Provides
    @Singleton
    fun provideCsvFileWriter(@ApplicationContext context: Context): CsvFileWriter {
        return RecordsCsvFileWriter(context)
    }
}
