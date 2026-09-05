package com.example.financesmanagementapp.data.local

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.example.financesmanagementapp.domain.model.CategoryName
import com.example.financesmanagementapp.domain.model.SubcategoryName
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
            amount;description;category;subcategory;date;currency
            -30000.0;Farmacia;${CategoryName.CATEGORY_HEALTH};${SubcategoryName.SUBCATEGORY_MEDICINE};2026-04-09;ARS
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()
        every { resources.getResourceEntryName(any()) } returns "ic_category_medicine"

        val result = parseCsvUseCase(inputStream)

        val record = result.records.first()
        assertEquals("Farmacia", record.description)
        assertEquals(-30000.0, record.amount, 0.001)
        assertEquals(CategoryName.CATEGORY_HEALTH, record.category.categoryName)
        assertEquals(SubcategoryName.SUBCATEGORY_MEDICINE, record.category.subcategoryName)
        assertEquals("2026-04-09", record.date)
        assertEquals("ARS", record.currency)
        assertEquals(1, result.records.size)
        assertTrue(result.errors.isEmpty())
    }

    @Test
    fun `given csv with utf-8 BOM before header when invoke then returns parsed record`() {
        val csvContent = "\uFEFF" + """
            amount;description;category;subcategory;date;currency
            -30000.0;Farmacia;${CategoryName.CATEGORY_HEALTH};${SubcategoryName.SUBCATEGORY_MEDICINE};2026-04-09;ARS
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()
        every { resources.getResourceEntryName(any()) } returns "ic_category_medicine"

        val result = parseCsvUseCase(inputStream)

        val record = result.records.first()
        assertEquals(-30000.0, record.amount, 0.001)
        assertEquals(CategoryName.CATEGORY_HEALTH, record.category.categoryName)
        assertEquals(1, result.records.size)
        assertTrue(result.errors.isEmpty())
    }

    @Test
    fun `given valid csv with reordered columns when invoke then returns parsed record`() {
        val csvContent = """
            description;amount;category;subcategory;currency;date
            Farmacia;-30000.0;${CategoryName.CATEGORY_HEALTH};${SubcategoryName.SUBCATEGORY_MEDICINE};ARS;2026-04-09
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()
        every { resources.getResourceEntryName(any()) } returns "ic_category_medicine"

        val result = parseCsvUseCase(inputStream)

        val record = result.records.first()
        assertEquals("Farmacia", record.description)
        assertEquals(-30000.0, record.amount, 0.001)
        assertEquals(CategoryName.CATEGORY_HEALTH, record.category.categoryName)
        assertEquals(SubcategoryName.SUBCATEGORY_MEDICINE, record.category.subcategoryName)
        assertEquals("2026-04-09", record.date)
        assertEquals("ARS", record.currency)
        assertTrue(result.errors.isEmpty())
    }

    @Test
    fun `given csv without description column when invoke then defaults to empty string`() {
        val csvContent = """
            amount;category;subcategory;date;currency
            -30000.0;${CategoryName.CATEGORY_HEALTH};${SubcategoryName.SUBCATEGORY_MEDICINE};2026-04-09;ARS
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
            description;category;subcategory;date;currency
            Farmacia;${CategoryName.CATEGORY_HEALTH};${SubcategoryName.SUBCATEGORY_MEDICINE};2026-04-09;ARS
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
            description;amount;subcategory;date;currency
            Farmacia;-100.23;${SubcategoryName.SUBCATEGORY_MEDICINE};2026-04-09;ARS
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()

        val result = parseCsvUseCase(inputStream)

        assertTrue(result.records.isEmpty())
        assertEquals(1, result.errors.size)
        val error = result.errors.first()
        assertTrue(error is ParseError.MissingField)
        assertEquals("category", (error as ParseError.MissingField).field)
    }

    @Test
    fun `given csv without subcategory column when invoke then returns missing field error`() {
        val csvContent = """
            description;amount;category;date;currency
            Farmacia;-100.23;${CategoryName.CATEGORY_HEALTH};2026-04-09;ARS
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()

        val result = parseCsvUseCase(inputStream)

        assertTrue(result.records.isEmpty())
        assertEquals(1, result.errors.size)
        val error = result.errors.first()
        assertTrue(error is ParseError.MissingField)
        assertEquals("subcategory", (error as ParseError.MissingField).field)
    }

    @Test
    fun `given csv without date column when invoke then returns missing field error`() {
        val csvContent = """
            amount;description;category;subcategory;currency
            -30000.0;Farmacia;${CategoryName.CATEGORY_HEALTH};${SubcategoryName.SUBCATEGORY_MEDICINE};ARS
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
            amount;description;category;subcategory;date
            -30000.0;Farmacia;${CategoryName.CATEGORY_HEALTH};${SubcategoryName.SUBCATEGORY_MEDICINE};2026-04-09
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
            amount;description;category;subcategory;date;currency
            abc;Farmacia;${CategoryName.CATEGORY_HEALTH};${SubcategoryName.SUBCATEGORY_MEDICINE};2026-04-09;ARS
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
    fun `given csv with blank category when invoke then returns missing field error`() {
        val csvContent = """
            amount;description;category;subcategory;date;currency
            -30000.0;Farmacia;;${SubcategoryName.SUBCATEGORY_MEDICINE};2026-04-09;ARS
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()

        val result = parseCsvUseCase(inputStream)

        assertTrue(result.records.isEmpty())
        assertEquals(1, result.errors.size)
    }

    @Test
    fun `given csv with blank subcategory when invoke then returns missing field error`() {
        val csvContent = """
            amount;description;category;subcategory;date;currency
            -30000.0;Farmacia;${CategoryName.CATEGORY_HEALTH};;2026-04-09;ARS
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()

        val result = parseCsvUseCase(inputStream)

        assertTrue(result.records.isEmpty())
        assertEquals(1, result.errors.size)
    }

    @Test
    fun `given csv with blank amount when invoke then returns missing field error`() {
        val csvContent = """
            amount;description;category;subcategory;date;currency
            ;Farmacia;${CategoryName.CATEGORY_HEALTH};${SubcategoryName.SUBCATEGORY_MEDICINE};2026-04-09;ARS
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()

        val result = parseCsvUseCase(inputStream)

        assertTrue(result.records.isEmpty())
        assertEquals(1, result.errors.size)
        assertTrue(result.errors.first() is ParseError.MissingField)
        assertEquals("amount", (result.errors.first() as ParseError.MissingField).field)
    }

    @Test
    fun `given csv with blank date when invoke then returns missing field error`() {
        val csvContent = """
            amount;description;category;subcategory;date;currency
            -30000.0;Farmacia;${CategoryName.CATEGORY_HEALTH};${SubcategoryName.SUBCATEGORY_MEDICINE};;ARS
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()

        val result = parseCsvUseCase(inputStream)

        assertTrue(result.records.isEmpty())
        assertEquals(1, result.errors.size)
        assertEquals("date", (result.errors.first() as ParseError.MissingField).field)
    }

    @Test
    fun `given csv with blank currency when invoke then returns missing field error`() {
        val csvContent = """
            amount;description;category;subcategory;date;currency
            -30000.0;Farmacia;${CategoryName.CATEGORY_HEALTH};${SubcategoryName.SUBCATEGORY_MEDICINE};2026-04-09;
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()

        val result = parseCsvUseCase(inputStream)

        assertTrue(result.records.isEmpty())
        assertEquals(1, result.errors.size)
        assertEquals("currency", (result.errors.first() as ParseError.MissingField).field)
    }

    @Test
    fun `given csv line with fewer columns than header when invoke then returns missing field error`() {
        val csvContent = """
            amount;description;category;subcategory;date;currency
            -30000.0;Farmacia
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
        val csvContent = "amount;description;category;subcategory;date;currency"
        val inputStream = csvContent.byteInputStream()

        val result = parseCsvUseCase(inputStream)

        assertTrue(result.records.isEmpty())
        assertEquals(1, result.errors.size)
        assertTrue(result.errors.first() is ParseError.EmptyFile)
    }

    @Test
    fun `given mix of valid and invalid lines when invoke then returns partial success`() {
        val csvContent = """
            amount;description;category;subcategory;date;currency
            -30000.0;Farmacia;${CategoryName.CATEGORY_HEALTH};${SubcategoryName.SUBCATEGORY_MEDICINE};2026-04-09;ARS
            abc;Farmacia;${CategoryName.CATEGORY_HEALTH};${SubcategoryName.SUBCATEGORY_MEDICINE};2026-04-09;ARS
            -100.0;;${CategoryName.CATEGORY_FOOD_AND_DRINKS};${SubcategoryName.SUBCATEGORY_MARKET};2026-04-10;ARS
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
            amount;description;category;subcategory;date;currency
            -100.0;Test;CATEGORY_DOES_NOT_EXIST;${SubcategoryName.SUBCATEGORY_MEDICINE};2026-01-01;ARS
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()
        every { resources.getResourceEntryName(any()) } returns "some_entry"

        val result = parseCsvUseCase(inputStream)

        assertEquals(CategoryName.WITHOUT_CATEGORY, result.records.first().category.categoryName)
        assertTrue(result.errors.isEmpty())
    }

    @Test
    fun `given csv line with only 3 columns when invoke then returns missing field error for subcategory`() {
        val csvContent = """
            amount;description;category;subcategory;date;currency
            -30000.0;Test;${CategoryName.CATEGORY_HEALTH}
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()

        val result = parseCsvUseCase(inputStream)

        assertTrue(result.records.isEmpty())
        assertEquals(1, result.errors.size)
        assertTrue(result.errors.first() is ParseError.MissingField)
    }

    @Test
    fun `given csv line with only 4 columns when invoke then returns missing field error for date`() {
        val csvContent = """
            amount;description;category;subcategory;date;currency
            -30000.0;Test;${CategoryName.CATEGORY_HEALTH};${SubcategoryName.SUBCATEGORY_MEDICINE}
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()

        val result = parseCsvUseCase(inputStream)

        assertTrue(result.records.isEmpty())
        assertEquals(1, result.errors.size)
        assertEquals("date", (result.errors.first() as ParseError.MissingField).field)
    }

    @Test
    fun `given csv line with only 5 columns when invoke then returns missing field error for currency`() {
        val csvContent = """
            amount;description;category;subcategory;date;currency
            -30000.0;Test;${CategoryName.CATEGORY_HEALTH};${SubcategoryName.SUBCATEGORY_MEDICINE};2026-04-09
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()

        val result = parseCsvUseCase(inputStream)

        assertTrue(result.records.isEmpty())
        assertEquals(1, result.errors.size)
        assertEquals("currency", (result.errors.first() as ParseError.MissingField).field)
    }

    @Test
    fun `given getResourceEntryName throws when invoke then still parses record`() {
        val csvContent = """
            amount;description;category;subcategory;date;currency
            -100.0;Test;${CategoryName.CATEGORY_FOOD_AND_DRINKS};${SubcategoryName.SUBCATEGORY_MARKET};2026-01-01;ARS
        """.trimIndent()
        val inputStream = csvContent.byteInputStream()
        every { resources.getResourceEntryName(any()) } throws Resources.NotFoundException()

        val result = parseCsvUseCase(inputStream)

        assertEquals(1, result.records.size)
        assertEquals(-100.0, result.records.first().amount, 0.001)
        assertTrue(result.errors.isEmpty())
    }
}
