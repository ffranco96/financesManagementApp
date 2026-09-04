package com.example.financesmanagementapp.data.crash

import com.example.financesmanagementapp.domain.crash.CrashReporter
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [CrashReporter] backed by Firebase Crashlytics.
 *
 * Crashlytics auto-initializes via its manifest-merged ContentProvider, and whether it actually
 * uploads anything is controlled by the `firebase_crashlytics_collection_enabled` manifest
 * meta-data (off on debug builds — see `app/build.gradle.kts`).
 */
@Singleton
class FirebaseCrashReporter @Inject constructor() : CrashReporter {

    private val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()

    override fun recordException(throwable: Throwable, message: String?) {
        message?.let { crashlytics.log(it) }
        crashlytics.recordException(throwable)
    }

    override fun log(message: String) {
        crashlytics.log(message)
    }

    override fun setCustomKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }
}
