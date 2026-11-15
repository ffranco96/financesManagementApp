package com.example.financesmanagementapp.data.local

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class RecordsCsvFileWriterTest {
    private lateinit var writer: RecordsCsvFileWriter
    private lateinit var context: Context
    private val createdFiles = mutableListOf<File>()

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        writer = RecordsCsvFileWriter(context)
    }

    @After
    fun tearDown() {
        createdFiles.forEach { if (it.exists()) it.delete() }
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    /** Creates a fresh destination document URI backed by a real file. */
    private fun newDestination(): Uri {
        val file = File(context.cacheDir, "test_${System.nanoTime()}.csv")
        createdFiles.add(file)
        return Uri.fromFile(file)
    }

    private fun backingFile(uri: Uri): File = File(requireNotNull(uri.path))

    // ── Tests ────────────────────────────────────────────────────────────

    @Test
    fun givenValidCsvContent_whenWriteCsv_thenDocumentIsCreatedWithCorrectContent() {
        val destination = newDestination()
        val csvContent = "amount; description; categoryName; date; currency\n" +
                "100.0; Registro de prueba; Comida y alimentos; 2026-01-15T10:30:00; ARS"

        val result = writer.writeCsv(destination, csvContent)

        assertTrue(result)
        assertTrue(backingFile(destination).exists())
        assertEquals(csvContent, backingFile(destination).readText())
    }

    @Test
    fun givenExistingDocument_whenWriteCsv_thenContentIsReplaced() {
        val destination = newDestination()
        backingFile(destination).writeText("contenido previo mucho mas largo que el nuevo")

        val result = writer.writeCsv(destination, "nuevo")

        assertTrue(result)
        assertEquals("nuevo", backingFile(destination).readText())
    }

    @Test
    fun givenWriteSuccess_whenWriteCsv_thenReturnsTrue() {
        val destination = newDestination()

        val result = writer.writeCsv(destination, "amount; description; categoryName; date; currency")

        assertTrue(result)
    }
}
