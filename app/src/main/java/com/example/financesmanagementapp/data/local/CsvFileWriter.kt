package com.example.financesmanagementapp.data.local

import android.net.Uri

/**
 * Abstraction for writing CSV content to a document chosen by the user.
 *
 * This interface decouples the CSV generation logic from the Android-specific
 * file I/O, enabling unit testing of use cases that depend on it.
 */
interface CsvFileWriter {
    /**
     * Writes the given [content] to the document identified by [uri].
     *
     * The destination comes from the Storage Access Framework
     * (`ActivityResultContracts.CreateDocument`), so the user picks both the
     * location and the name of the exported file.
     *
     * @param uri The destination document URI returned by the system picker.
     * @param content The full CSV string to write (header + records).
     * @return `true` if the write succeeded, `false` if the destination could
     * not be opened.
     */
    fun writeCsv(uri: Uri, content: String): Boolean
}
