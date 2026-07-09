package com.example.financesmanagementapp.data.local

/**
 * Abstraction for writing CSV content to a file.
 *
 * This interface decouples the CSV generation logic from the Android-specific
 * file I/O, enabling unit testing of use cases that depend on it.
 */
interface CsvFileWriter {
    /**
     * Writes the given [content] to a file named [fileName] in CSV format.
     *
     * @param content The full CSV string to write (header + records).
     * @param fileName The name of the file to create (e.g. "records_20260409_120000.csv").
     * @return `true` if the write succeeded, `false` otherwise.
     * @throws FileAlreadyExistsException if a file with the same name already exists.
     */
    fun writeCsv(content: String, fileName: String): Boolean
}
