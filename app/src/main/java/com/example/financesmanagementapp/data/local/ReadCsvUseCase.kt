package com.example.financesmanagementapp.data.local

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import javax.inject.Inject

/**
 * Reads a CSV file from a given URI.
 */
class ReadCsvUseCase @Inject constructor(
    @ApplicationContext private val context: Context
){
    /**
     * Receives the URI and reads the csv in the injected context.
     * @param uri The URI of the CSV file.
     * @return The InputStream of the CSV file to be converted to Record objects.
     */
    operator fun invoke(uri: Uri): InputStream?{
        return try {
            context.contentResolver.openInputStream(uri)
        } catch(e: Exception){
            return null
        }
    }
}