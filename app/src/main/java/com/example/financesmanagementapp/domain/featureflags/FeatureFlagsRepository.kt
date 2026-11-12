package com.example.financesmanagementapp.domain.featureflags

/**
 * Abstraction over the feature-flag backend (Firebase Remote Config).
 *
 * Keeps the Firebase SDK out of the domain/navigation layers.
 */
interface FeatureFlagsRepository {
    /** Whether the login screen should be shown on app start. Defaults to `false`. */
    fun isLoginEnabled(): Boolean
}
