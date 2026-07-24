package com.example.financesmanagementapp.data.local

import android.content.Context
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.BufferedWriter
import java.io.File
import java.io.OutputStreamWriter
import javax.inject.Inject

/**
 * Implementation of [CsvFileWriter] that writes CSV content
 * to a file in the application's internal storage via [Context].
 *
 * Uses [Context.filesDir] as the target directory and
 * [Context.contentResolver] to open the output stream.
 *
 * @param context Application context injected by Hilt.
 */
class RecordsCsvFileWriter @Inject constructor(
    @ApplicationContext private val context: Context
) : CsvFileWriter {

    /**
     * Writes [content] to a file named [fileName] in the app's internal storage.
     *
     * The file is created under [Context.filesDir]. If a file with the same
     * name already exists, a [FileAlreadyExistsException] is thrown.
     *
     * @param content The full CSV string to write.
     * @param fileName The name of the file to create.
     * @return `true` if the write succeeded.
     * @throws FileAlreadyExistsException if the file already exists.
     */
    override fun writeCsv(content: String, fileName: String): Boolean {
        val directory = context.filesDir
        val file = File(directory, fileName)
        if (file.exists()) {
            throw FileAlreadyExistsException(file)
        }

        context.contentResolver.openOutputStream(file.toUri())?.use { outputStream ->
            BufferedWriter(OutputStreamWriter(outputStream)).use { writer ->
                writer.write(content)
            }
        } ?: return false
        return true
    }
}
