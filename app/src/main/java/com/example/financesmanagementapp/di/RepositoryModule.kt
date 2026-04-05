package com.example.financesmanagementapp.di

import com.example.financesmanagementapp.data.repository.RecordsRepository
import com.example.financesmanagementapp.data.repository.RecordsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * Hilt module that provides the implementation to the domain repositories.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRecordsRepository(recordsRepositoryImpl: RecordsRepositoryImpl): RecordsRepository
}