package com.example.financesmanagementapp.data.featureflags

import com.example.financesmanagementapp.BuildConfig
import com.example.financesmanagementapp.domain.featureflags.FeatureFlagsRepository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [FeatureFlagsRepository] backed by Firebase Remote Config.
 *
 * Remote Config persists the last successfully activated value on disk, so [isLoginEnabled]
 * reads it synchronously and instantly, offline or online, without waiting on the network. The
 * [FirebaseRemoteConfig.fetchAndActivate] call kicked off here runs in the background and only
 * updates the value the *next* time the app starts — it never blocks or changes the current
 * session's navigation decision.
 */
@Singleton
class FirebaseFeatureFlagsRepository @Inject constructor() : FeatureFlagsRepository {

    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance().apply {
        if (BuildConfig.DEBUG) {
            // Bypasses the 12h production throttle so flag changes are visible on the next
            // debug run (when the app starts) instead of 12 hours later.
            setConfigSettingsAsync(
                FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(0)
                    .build()
            )
        }
        setDefaultsAsync(mapOf(KEY_LOGIN_ENABLED to false))
        fetchAndActivate()
    }

    override fun isLoginEnabled(): Boolean = remoteConfig.getBoolean(KEY_LOGIN_ENABLED)

    private companion object {
        const val KEY_LOGIN_ENABLED = "login_enabled"
    }
}
