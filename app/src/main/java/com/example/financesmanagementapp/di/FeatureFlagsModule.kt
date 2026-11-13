package com.example.financesmanagementapp.di

import com.example.financesmanagementapp.data.featureflags.FirebaseFeatureFlagsRepository
import com.example.financesmanagementapp.domain.featureflags.FeatureFlagsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that binds the [FeatureFlagsRepository] implementation.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class FeatureFlagsModule {
    @Binds
    @Singleton
    abstract fun bindFeatureFlagsRepository(impl: FirebaseFeatureFlagsRepository): FeatureFlagsRepository
}
