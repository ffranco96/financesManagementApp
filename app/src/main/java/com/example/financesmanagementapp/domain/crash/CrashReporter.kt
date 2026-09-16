package com.example.financesmanagementapp.domain.crash

/**
 * Abstraction over the crash-reporting backend (Firebase Crashlytics).
 *
 * Keeps the Firebase SDK out of the domain/data layers so callers stay unit-testable and the
 * backend can be swapped. Inject it wherever an exception is currently swallowed in a `catch`
 * block (see [com.example.financesmanagementapp.data.repository.ConfigRepositoryImpl],
 * [com.example.financesmanagementapp.data.local.ExportCsvUseCase],
 * [com.example.financesmanagementapp.data.local.ReadCsvUseCase]).
 */
interface CrashReporter {
    /**
     * Records a non-fatal [throwable]. If [message] is given it is attached as a breadcrumb log
     * right before the exception so it shows up in the Crashlytics session.
     */
    fun recordException(throwable: Throwable, message: String? = null)

    /** Adds a breadcrumb log line to the current Crashlytics session. */
    fun log(message: String)

    /** Sets a custom key/value shown on every subsequent report. */
    fun setCustomKey(key: String, value: String)
}
