package com.example.financesmanagementapp.data.local

import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.CategoryName
import com.example.financesmanagementapp.domain.model.Record
import com.example.financesmanagementapp.domain.model.SubcategoryName
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ExportCsvUseCaseTest {

    private val csvFileWriter: CsvFileWriter = mockk()
    private lateinit var exportCsvUseCase: ExportCsvUseCase

    @Before
    fun setUp() {
        exportCsvUseCase = ExportCsvUseCase(csvFileWriter)
    }

    @Test
    fun `given records when invoke then returns true`() = runTest {
        every { csvFileWriter.writeCsv(any(), any()) } returns true
        val records = listOf(
            Record(amount = -30000.0, description = "Farmacia", category = Category(CategoryName.CATEGORY_HEALTH, SubcategoryName.SUBCATEGORY_MEDICINE), date = "2026-04-09", currency = "ARS")
        )

        val result = exportCsvUseCase(records)

        assertTrue(result)
    }

    @Test
    fun `given records when invoke then writes correct csv header`() = runTest {
        val contentSlot = slot<String>()
        every { csvFileWriter.writeCsv(capture(contentSlot), any()) } returns true

        exportCsvUseCase(emptyList())

        val lines = contentSlot.captured.lines()
        assertEquals("amount; description; category; subcategory; date; currency", lines[0])
    }

    @Test
    fun `given single record when invoke then csv contains header and record`() = runTest {
        val contentSlot = slot<String>()
        every { csvFileWriter.writeCsv(capture(contentSlot), any()) } returns true
        val records = listOf(
            Record(amount = -30000.0, description = "Farmacia", category = Category(CategoryName.CATEGORY_HEALTH, SubcategoryName.SUBCATEGORY_MEDICINE), date = "2026-04-09", currency = "ARS")
        )

        exportCsvUseCase(records)

        val lines = contentSlot.captured.lines().filter { it.isNotBlank() }
        assertEquals(2, lines.size)
        assertEquals("-30000.0; Farmacia; ${CategoryName.CATEGORY_HEALTH}; ${SubcategoryName.SUBCATEGORY_MEDICINE}; 2026-04-09; ARS", lines[1])
    }

    @Test
    fun `given multiple records when invoke then csv contains all records`() = runTest {
        val contentSlot = slot<String>()
        every { csvFileWriter.writeCsv(capture(contentSlot), any()) } returns true
        val records = listOf(
            Record(amount = -30000.0, description = "Farmacia", category = Category(CategoryName.CATEGORY_HEALTH, SubcategoryName.SUBCATEGORY_MEDICINE), date = "2026-04-09", currency = "ARS"),
            Record(amount = -100.0, description = "Almuerzo", category = Category(CategoryName.CATEGORY_FOOD_AND_DRINKS, SubcategoryName.SUBCATEGORY_MARKET), date = "2026-04-10", currency = "ARS"),
            Record(amount = 200.0, description = "Sueldo", category = Category(CategoryName.CATEGORY_INCOME, SubcategoryName.SUBCATEGORY_SALARY), date = "2026-04-11", currency = "ARS")
        )

        exportCsvUseCase(records)

        val lines = contentSlot.captured.lines().filter { it.isNotBlank() }
        assertEquals(4, lines.size)
        assertEquals("-30000.0; Farmacia; ${CategoryName.CATEGORY_HEALTH}; ${SubcategoryName.SUBCATEGORY_MEDICINE}; 2026-04-09; ARS", lines[1])
        assertEquals("-100.0; Almuerzo; ${CategoryName.CATEGORY_FOOD_AND_DRINKS}; ${SubcategoryName.SUBCATEGORY_MARKET}; 2026-04-10; ARS", lines[2])
        assertEquals("200.0; Sueldo; ${CategoryName.CATEGORY_INCOME}; ${SubcategoryName.SUBCATEGORY_SALARY}; 2026-04-11; ARS", lines[3])
    }

    @Test
    fun `given empty records when invoke then csv contains only header`() = runTest {
        val contentSlot = slot<String>()
        every { csvFileWriter.writeCsv(capture(contentSlot), any()) } returns true

        exportCsvUseCase(emptyList())

        val lines = contentSlot.captured.lines().filter { it.isNotBlank() }
        assertEquals(1, lines.size)
        assertEquals("amount; description; category; subcategory; date; currency", lines[0])
    }

    @Test
    fun `given writer returns false when invoke then returns false`() = runTest {
        every { csvFileWriter.writeCsv(any(), any()) } returns false

        val result = exportCsvUseCase(emptyList())

        assertFalse(result)
    }

    @Test
    fun `given writer throws exception when invoke then returns false`() = runTest {
        every { csvFileWriter.writeCsv(any(), any()) } throws RuntimeException("Write failed")

        val result = exportCsvUseCase(emptyList())

        assertFalse(result)
    }

    @Test
    fun `given records when invoke then writer receives csv content with semicolon delimiter`() = runTest {
        val contentSlot = slot<String>()
        every { csvFileWriter.writeCsv(capture(contentSlot), any()) } returns true
        val records = listOf(
            Record(amount = 50.0, description = "Test", category = Category(CategoryName.CATEGORY_FOOD_AND_DRINKS, SubcategoryName.SUBCATEGORY_MARKET), date = "2026-01-01", currency = "USD")
        )

        exportCsvUseCase(records)

        val content = contentSlot.captured
        assertTrue(content.contains(";"))
        assertFalse(content.contains(","))
    }

    @Test
    fun `given records when invoke then writer receives filename with csv extension`() = runTest {
        val fileNameSlot = slot<String>()
        every { csvFileWriter.writeCsv(any(), capture(fileNameSlot)) } returns true
        val records = listOf(
            Record(amount = 50.0, description = "Test", category = Category(CategoryName.CATEGORY_FOOD_AND_DRINKS, SubcategoryName.SUBCATEGORY_MARKET), date = "2026-01-01", currency = "USD")
        )

        exportCsvUseCase(records)

        assertTrue(fileNameSlot.captured.startsWith("records_"))
        assertTrue(fileNameSlot.captured.endsWith(".csv"))
    }
}
