package com.example.financesmanagementapp.di

import com.example.financesmanagementapp.data.crash.FirebaseCrashReporter
import com.example.financesmanagementapp.domain.crash.CrashReporter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that binds the [CrashReporter] implementation.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class CrashModule {
    @Binds
    @Singleton
    abstract fun bindCrashReporter(impl: FirebaseCrashReporter): CrashReporter
}
