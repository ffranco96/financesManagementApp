package com.example.financesmanagementapp.data.local

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.example.financesmanagementapp.domain.model.Category
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ParseCsvUseCaseTest {
    private val context: Context = mockk()
    private val resources: Resources = mockk()
    private lateinit var parseCsvUseCase: ParseCsvUseCase

    @Before
    fun setUp() {
        parseCsvUseCase = ParseCsvUseCase(context)
        every { context.resources } returns resources
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    @Test
    fun `given valid csv line when invoke then returns parsed record`() {
        val csvContent = """
            amount;description;categoryName;date;currency
            -30000.0;Farmacia;${Category.CATEGORY_MEDICINE};2026-04-09;ARS
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()
        every { resources.getResourceEntryName(any()) } returns "ic_category_medicine"

        val result = parseCsvUseCase(inputStream)

        val record = result.records.first()
        assertEquals("Farmacia", record.description)
        assertEquals(-30000.0, record.amount, 0.001)
        assertEquals(Category.CATEGORY_MEDICINE, record.category.categoryName)
        assertEquals("2026-04-09", record.date)
        assertEquals("ARS", record.currency)
        assertEquals(1, result.records.size)
        assertTrue(result.errors.isEmpty())
    }

    @Test
    fun `given valid csv with reordered columns when invoke then returns parsed record`() {
        val csvContent = """
            description;amount;categoryName;currency;date
            Farmacia;-30000.0;${Category.CATEGORY_MEDICINE};ARS;2026-04-09
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()
        every { resources.getResourceEntryName(any()) } returns "ic_category_medicine"

        val result = parseCsvUseCase(inputStream)

        val record = result.records.first()
        assertEquals("Farmacia", record.description)
        assertEquals(-30000.0, record.amount, 0.001)
        assertEquals(Category.CATEGORY_MEDICINE, record.category.categoryName)
        assertEquals("2026-04-09", record.date)
        assertEquals("ARS", record.currency)
        assertTrue(result.errors.isEmpty())
    }

    @Test
    fun `given csv without description column when invoke then defaults to empty string`() {
        val csvContent = """
            amount;categoryName;date;currency
            -30000.0;${Category.CATEGORY_MEDICINE};2026-04-09;ARS
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()
        every { resources.getResourceEntryName(any()) } returns "ic_category_medicine"

        val result = parseCsvUseCase(inputStream)

        assertEquals("", result.records.first().description)
        assertTrue(result.errors.isEmpty())
    }

    @Test
    fun `given csv without amount column when invoke then returns missing field error`() {
        val csvContent = """
            description;categoryName;date;currency
            Farmacia;${Category.CATEGORY_MEDICINE};2026-04-09;ARS
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()

        val result = parseCsvUseCase(inputStream)

        assertTrue(result.records.isEmpty())
        assertEquals(1, result.errors.size)
        val error = result.errors.first()
        assertTrue(error is ParseError.MissingField)
        assertEquals("amount", (error as ParseError.MissingField).field)
    }

    @Test
    fun `given csv without category column when invoke then returns missing field error`() {
        val csvContent = """
            description;amount;date;currency
            Farmacia;-100.23;2026-04-09;ARS
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()

        val result = parseCsvUseCase(inputStream)

        assertTrue(result.records.isEmpty())
        assertEquals(1, result.errors.size)
        val error = result.errors.first()
        assertTrue(error is ParseError.MissingField)
        assertEquals("categoryName", (error as ParseError.MissingField).field)
    }

    @Test
    fun `given csv without date column when invoke then returns missing field error`() {
        val csvContent = """
            amount;description;categoryName;currency
            -30000.0;Farmacia;${Category.CATEGORY_MEDICINE};ARS
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()

        val result = parseCsvUseCase(inputStream)

        assertTrue(result.records.isEmpty())
        assertEquals(1, result.errors.size)
        val error = result.errors.first()
        assertTrue(error is ParseError.MissingField)
        assertEquals("date", (error as ParseError.MissingField).field)
    }

    @Test
    fun `given csv without currency column when invoke then returns missing field error`() {
        val csvContent = """
            amount;description;categoryName;date
            -30000.0;Farmacia;${Category.CATEGORY_MEDICINE};2026-04-09
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()

        val result = parseCsvUseCase(inputStream)

        assertTrue(result.records.isEmpty())
        assertEquals(1, result.errors.size)
        val error = result.errors.first()
        assertTrue(error is ParseError.MissingField)
        assertEquals("currency", (error as ParseError.MissingField).field)
    }

    @Test
    fun `given csv with non numeric amount when invoke then returns format error`() {
        val csvContent = """
            amount;description;categoryName;date;currency
            abc;Farmacia;${Category.CATEGORY_MEDICINE};2026-04-09;ARS
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()

        val result = parseCsvUseCase(inputStream)

        assertTrue(result.records.isEmpty())
        assertEquals(1, result.errors.size)
        val error = result.errors.first()
        assertTrue(error is ParseError.FormatError)
        assertEquals("amount", (error as ParseError.FormatError).field)
        assertEquals("abc", (error as ParseError.FormatError).value)
    }

    @Test
    fun `given csv with blank categoryName when invoke then returns missing field error`() {
        val csvContent = """
            amount;description;categoryName;date;currency
            -30000.0;Farmacia;;2026-04-09;ARS
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()

        val result = parseCsvUseCase(inputStream)

        assertTrue(result.records.isEmpty())
        assertEquals(1, result.errors.size)
    }

    @Test
    fun `given empty content when invoke then returns empty file error`() {
        val inputStream = "".byteInputStream()

        val result = parseCsvUseCase(inputStream)

        assertTrue(result.records.isEmpty())
        assertEquals(1, result.errors.size)
        assertTrue(result.errors.first() is ParseError.EmptyFile)
    }

    @Test
    fun `given header only when invoke then returns empty file error`() {
        val csvContent = "amount;description;categoryName;date;currency"
        val inputStream = csvContent.byteInputStream()

        val result = parseCsvUseCase(inputStream)

        assertTrue(result.records.isEmpty())
        assertEquals(1, result.errors.size)
        assertTrue(result.errors.first() is ParseError.EmptyFile)
    }

    @Test
    fun `given mix of valid and invalid lines when invoke then returns partial success`() {
        val csvContent = """
            amount;description;categoryName;date;currency
            -30000.0;Farmacia;${Category.CATEGORY_MEDICINE};2026-04-09;ARS
            abc;Farmacia;${Category.CATEGORY_MEDICINE};2026-04-09;ARS
            -100.0;;${Category.CATEGORY_FOOD};2026-04-10;ARS
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()
        every { resources.getResourceEntryName(any()) } returns "some_entry"

        val result = parseCsvUseCase(inputStream)

        assertEquals(2, result.records.size)
        assertEquals(1, result.errors.size)
        val error = result.errors.first()
        assertTrue(error is ParseError.FormatError)
    }

    @Test
    fun `given csv with unknown category when invoke then uses default category`() {
        val csvContent = """
            amount;description;categoryName;date;currency
            -100.0;Test;${Category.CATEGORY_MISSING};2026-01-01;ARS
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()
        every { resources.getResourceEntryName(any()) } returns "some_entry"

        val result = parseCsvUseCase(inputStream)

        assertEquals(Category.CATEGORY_MISSING, result.records.first().category.categoryName)
        assertTrue(result.errors.isEmpty())
    }
}
