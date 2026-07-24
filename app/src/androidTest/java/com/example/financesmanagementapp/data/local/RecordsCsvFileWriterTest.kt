package com.example.financesmanagementapp.data.local

import android.content.Context
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

    private fun uniqueFileName() = "test_${System.nanoTime()}.csv"

    private fun registerFile(fileName: String): File {
        val file = File(context.filesDir, fileName)
        createdFiles.add(file)
        return file
    }

    // ── Tests ────────────────────────────────────────────────────────────

    @Test
    fun givenValidCsvContent_whenWriteCsv_thenFileIsCreatedWithCorrectContent() {
        val fileName = uniqueFileName()
        val file = registerFile(fileName)
        val csvContent = "amount; description; categoryName; date; currency\n" +
                "100.0; Registro de prueba; Comida y alimentos; 2026-01-15T10:30:00; ARS"

        val result = writer.writeCsv(csvContent, fileName)

        assertTrue(result)
        assertTrue(file.exists())
        assertEquals(csvContent, file.readText())
    }

    @Test(expected = FileAlreadyExistsException::class)
    fun givenExistingFile_whenWriteCsv_thenThrowsFileAlreadyExistsException() {
        val fileName = uniqueFileName()
        registerFile(fileName)
        val file = File(context.filesDir, fileName)
        file.writeText(" contenido previo ")

        writer.writeCsv("nuevo contenido", fileName)
    }

    @Test
    fun givenWriteSuccess_whenWriteCsv_thenReturnsTrue() {
        val fileName = uniqueFileName()
        registerFile(fileName)

        val result = writer.writeCsv("amount; description; categoryName; date; currency", fileName)

        assertTrue(result)
    }
}
