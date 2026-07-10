package com.example.financesmanagementapp.data.local.mapper

import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.Record
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
    fun `given Record when toEntity then maps categoryName from category object`() {
        // Given
        val record = Record(category = Category(Category.CATEGORY_MEDICINE))

        // When
        val entity = record.toEntity()

        // Then
        assertEquals(Category.CATEGORY_MEDICINE, entity.categoryName)
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
        val entity = com.example.financesmanagementapp.data.local.entities.RecordEntity(
            accountId = 3, amount = 100.0, description = "Test",
            categoryName = Category.CATEGORY_FOOD, date = "2026-01-01", currency = "USD"
        )
        val completeCategory = Category(Category.CATEGORY_FOOD)

        // When
        val record = entity.toDomain(completeCategory)

        // Then
        assertEquals(3, record.accountId)
    }

    @Test
    fun `given RecordEntity when toDomain then maps amount correctly`() {
        // Given
        val entity = com.example.financesmanagementapp.data.local.entities.RecordEntity(
            amount = -500.0, description = "Test",
            categoryName = Category.CATEGORY_FOOD, date = "2026-01-01", currency = "USD"
        )
        val completeCategory = Category(Category.CATEGORY_FOOD)

        // When
        val record = entity.toDomain(completeCategory)

        // Then
        assertEquals(-500.0, record.amount, 0.001)
    }

    @Test
    fun `given RecordEntity when toDomain then maps description correctly`() {
        // Given
        val entity = com.example.financesmanagementapp.data.local.entities.RecordEntity(
            amount = 100.0, description = "Almuerzo en la ofi",
            categoryName = Category.CATEGORY_FOOD, date = "2026-01-01", currency = "USD"
        )
        val completeCategory = Category(Category.CATEGORY_FOOD)

        // When
        val record = entity.toDomain(completeCategory)

        // Then
        assertEquals("Almuerzo en la ofi", record.description)
    }

    @Test
    fun `given RecordEntity when toDomain then uses provided completeCategory`() {
        // Given
        val entity = com.example.financesmanagementapp.data.local.entities.RecordEntity(
            amount = 100.0, description = "Test",
            categoryName = Category.CATEGORY_FOOD, date = "2026-01-01", currency = "USD"
        )
        val completeCategory = Category.fromName(Category.CATEGORY_FOOD,)

        // When
        val record = entity.toDomain(completeCategory)

        // Then
        assertEquals(Category.CATEGORY_FOOD, record.category.categoryName)
    }

    @Test
    fun `given RecordEntity when toDomain then maps date correctly`() {
        // Given
        val entity = com.example.financesmanagementapp.data.local.entities.RecordEntity(
            amount = 100.0, description = "Test",
            categoryName = Category.CATEGORY_FOOD, date = "2026-04-09T12:00:00", currency = "USD"
        )
        val completeCategory = Category(Category.CATEGORY_FOOD)

        // When
        val record = entity.toDomain(completeCategory)

        // Then
        assertEquals("2026-04-09T12:00:00", record.date)
    }

    @Test
    fun `given RecordEntity when toDomain then maps currency correctly`() {
        // Given
        val entity = com.example.financesmanagementapp.data.local.entities.RecordEntity(
            amount = 100.0, description = "Test",
            categoryName = Category.CATEGORY_FOOD, date = "2026-01-01", currency = "EUR"
        )
        val completeCategory = Category(Category.CATEGORY_FOOD)

        // When
        val record = entity.toDomain(completeCategory)

        // Then
        assertEquals("EUR", record.currency)
    }
}
