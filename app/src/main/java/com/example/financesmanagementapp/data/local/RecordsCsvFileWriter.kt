package com.example.financesmanagementapp.data.local

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import javax.inject.Inject

/**
 * Implementation of [CsvFileWriter] that writes CSV content to a document
 * selected by the user through the Storage Access Framework.
 *
 * The destination [Uri] comes from `ActivityResultContracts.CreateDocument`,
 * so the file ends up in a user-visible location (e.g. Downloads) and survives
 * an app uninstall. No storage permissions are required.
 *
 * @param context Application context injected by Hilt.
 */
class RecordsCsvFileWriter @Inject constructor(
    @ApplicationContext private val context: Context
) : CsvFileWriter {

    /**
     * Writes [content] to the document identified by [uri].
     *
     * The stream is opened in truncate mode so exporting over an existing
     * document replaces its content instead of appending to it.
     *
     * @param uri The destination document URI returned by the system picker.
     * @param content The full CSV string to write.
     * @return `true` if the write succeeded, `false` if the destination could
     * not be opened.
     */
    override fun writeCsv(uri: Uri, content: String): Boolean {
        val outputStream = context.contentResolver.openOutputStream(uri, WRITE_TRUNCATE_MODE)
            ?: return false

        outputStream.use { stream ->
            BufferedWriter(OutputStreamWriter(stream, Charsets.UTF_8)).use { writer ->
                writer.write(content)
            }
        }
        return true
    }

    private companion object {
        /**
         * `wt` truncates the document before writing, preventing leftover
         * bytes from a previous export.
         */
        const val WRITE_TRUNCATE_MODE = "wt"
    }
}
