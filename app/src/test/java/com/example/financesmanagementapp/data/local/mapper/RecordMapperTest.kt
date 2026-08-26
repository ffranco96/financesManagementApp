package com.example.financesmanagementapp.data.local.mapper

import com.example.financesmanagementapp.data.local.entities.RecordEntity
import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.CategoryName
import com.example.financesmanagementapp.domain.model.Record
import com.example.financesmanagementapp.domain.model.SubcategoryName
import org.junit.Assert.assertEquals
import org.junit.Test

class RecordMapperTest {

    // --- toEntity ---

    @Test
    fun `given Record when toEntity then maps amount correctly`() {
        // Given
        val record = Record(amount = -30000.0)

        // When
        val entity = record.toEntity()

        // Then
        assertEquals(-30000.0, entity.amount, 0.001)
    }

    @Test
    fun `given Record when toEntity then maps amount with decimals correctly`() {
        // Given
        val record = Record(amount = -30000.99)

        // When
        val entity = record.toEntity()

        // Then
        assertEquals(-30000.99, entity.amount, 0.001)
    }

    @Test
    fun `given Record when toEntity then maps description correctly`() {
        // Given
        val record = Record(description = "Medicamentos de la farmacia")

        // When
        val entity = record.toEntity()

        // Then
        assertEquals("Medicamentos de la farmacia", entity.description)
    }

    @Test
    fun `given Record when toEntity then maps category and subcategory names from category object`() {
        // Given
        val record = Record(
            category = Category(CategoryName.CATEGORY_HEALTH, SubcategoryName.SUBCATEGORY_MEDICINE)
        )

        // When
        val entity = record.toEntity()

        // Then
        assertEquals("CATEGORY_HEALTH", entity.category)
        assertEquals("SUBCATEGORY_MEDICINE", entity.subcategory)
    }

    @Test
    fun `given Record with default category when toEntity then maps to WITHOUT_CATEGORY and SUBCATEGORY_NONE`() {
        // Given
        val record = Record(category = Category())

        // When
        val entity = record.toEntity()

        // Then
        assertEquals("WITHOUT_CATEGORY", entity.category)
        assertEquals("SUBCATEGORY_NONE", entity.subcategory)
    }

    @Test
    fun `given Record when toEntity then maps date correctly`() {
        // Given
        val record = Record(date = "2026-07-09T12:00:00")

        // When
        val entity = record.toEntity()

        // Then
        assertEquals("2026-07-09T12:00:00", entity.date)
    }

    @Test
    fun `given Record when toEntity then maps currency correctly`() {
        // Given
        val record = Record(currency = "ARS")

        // When
        val entity = record.toEntity()

        // Then
        assertEquals("ARS", entity.currency)
    }

    @Test
    fun `given Record when toEntity then maps accountId correctly`() {
        // Given
        val record = Record(accountId = 5)

        // When
        val entity = record.toEntity()

        // Then
        assertEquals(5, entity.accountId)
    }

    @Test
    fun `given Record with default accountId when toEntity then maps accountId as 0`() {
        // Given
        val record = Record()

        // When
        val entity = record.toEntity()

        // Then
        assertEquals(0, entity.accountId)
    }

    // --- toDomain ---

    @Test
    fun `given RecordEntity when toDomain then maps accountId correctly`() {
        // Given
        val entity = RecordEntity(
            accountId = 3, amount = 100.0, description = "Test",
            category = "CATEGORY_FOOD_AND_DRINKS", subcategory = "SUBCATEGORY_MARKET",
            date = "2026-01-01", currency = "USD"
        )
        val completeCategory = Category.fromCategoryAndSubcategory("CATEGORY_FOOD_AND_DRINKS", "SUBCATEGORY_MARKET")

        // When
        val record = entity.toDomain(completeCategory)

        // Then
        assertEquals(3, record.accountId)
    }

    @Test
    fun `given RecordEntity when toDomain then maps amount correctly`() {
        // Given
        val entity = RecordEntity(
            amount = -500.0, description = "Test",
            category = "CATEGORY_FOOD_AND_DRINKS", subcategory = "SUBCATEGORY_MARKET",
            date = "2026-01-01", currency = "USD"
        )
        val completeCategory = Category.fromCategoryAndSubcategory("CATEGORY_FOOD_AND_DRINKS", "SUBCATEGORY_MARKET")

        // When
        val record = entity.toDomain(completeCategory)

        // Then
        assertEquals(-500.0, record.amount, 0.001)
    }

    @Test
    fun `given RecordEntity when toDomain then maps description correctly`() {
        // Given
        val entity = RecordEntity(
            amount = 100.0, description = "Almuerzo en la ofi",
            category = "CATEGORY_FOOD_AND_DRINKS", subcategory = "SUBCATEGORY_MARKET",
            date = "2026-01-01", currency = "USD"
        )
        val completeCategory = Category.fromCategoryAndSubcategory("CATEGORY_FOOD_AND_DRINKS", "SUBCATEGORY_MARKET")

        // When
        val record = entity.toDomain(completeCategory)

        // Then
        assertEquals("Almuerzo en la ofi", record.description)
    }

    @Test
    fun `given RecordEntity when toDomain then uses provided completeCategory`() {
        // Given
        val entity = RecordEntity(
            amount = 100.0, description = "Test",
            category = "CATEGORY_FOOD_AND_DRINKS", subcategory = "SUBCATEGORY_MARKET",
            date = "2026-01-01", currency = "USD"
        )
        val completeCategory = Category.fromCategoryAndSubcategory("CATEGORY_FOOD_AND_DRINKS", "SUBCATEGORY_MARKET")

        // When
        val record = entity.toDomain(completeCategory)

        // Then
        assertEquals(CategoryName.CATEGORY_FOOD_AND_DRINKS, record.category.categoryName)
        assertEquals(SubcategoryName.SUBCATEGORY_MARKET, record.category.subcategoryName)
    }

    @Test
    fun `given RecordEntity when toDomain then maps date correctly`() {
        // Given
        val entity = RecordEntity(
            amount = 100.0, description = "Test",
            category = "CATEGORY_FOOD_AND_DRINKS", subcategory = "SUBCATEGORY_MARKET",
            date = "2026-04-09T12:00:00", currency = "USD"
        )
        val completeCategory = Category.fromCategoryAndSubcategory("CATEGORY_FOOD_AND_DRINKS", "SUBCATEGORY_MARKET")

        // When
        val record = entity.toDomain(completeCategory)

        // Then
        assertEquals("2026-04-09T12:00:00", record.date)
    }

    @Test
    fun `given RecordEntity when toDomain then maps currency correctly`() {
        // Given
        val entity = RecordEntity(
            amount = 100.0, description = "Test",
            category = "CATEGORY_FOOD_AND_DRINKS", subcategory = "SUBCATEGORY_MARKET",
            date = "2026-01-01", currency = "EUR"
        )
        val completeCategory = Category.fromCategoryAndSubcategory("CATEGORY_FOOD_AND_DRINKS", "SUBCATEGORY_MARKET")

        // When
        val record = entity.toDomain(completeCategory)

        // Then
        assertEquals("EUR", record.currency)
    }
}
