package com.example.financesmanagementapp.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import javax.inject.Inject

/**
 * Loads a CSV file from the assets folder.
 * Note: Just used for testing purposes.
 */
class LoadCsvFromAssetsUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(): InputStream {
        return context.assets.open("records.csv")  // solo para pruebas
    }
}