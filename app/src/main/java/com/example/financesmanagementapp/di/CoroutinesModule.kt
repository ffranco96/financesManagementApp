package com.example.financesmanagementapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

/**
 * Qualifier for the dispatcher used to run blocking I/O work (disk, database).
 *
 * Injecting it instead of referencing [Dispatchers.IO] directly keeps ViewModels testable:
 * tests can supply a `TestDispatcher` and drive those coroutines deterministically.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

/**
 * Hilt module that provides the coroutine dispatchers used across the app.
 */
@Module
@InstallIn(SingletonComponent::class)
object CoroutinesModule {

    /**
     * Provides the dispatcher for blocking I/O operations.
     *
     * @return [Dispatchers.IO].
     */
    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
