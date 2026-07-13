package com.example.financesmanagementapp.data.local.entities

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RecordEntityTest {

    private fun buildEntity(date: String) = RecordEntity(
        amount = 0.0,
        description = "",
        categoryName = "",
        date = date,
        currency = "ARS"
    )

    // ── LocalDateTime format (yyyy-MM-dd'T'HH:mm:ss) ──────────────────

    @Test
    fun `given earlier date when compareTo then returns negative`() {
        val earlier = buildEntity("2026-01-15T10:00:00")
        val later = buildEntity("2026-01-15T12:00:00")

        val result = earlier.compareTo(later)

        assertTrue(result < 0)
    }

    @Test
    fun `given same datetime when compareTo then returns zero`() {
        val date1 = buildEntity("2026-01-15T10:30:00")
        val date2 = buildEntity("2026-01-15T10:30:00")

        val result = date1.compareTo(date2)

        assertEquals(0, result)
    }

    @Test
    fun `given later date when compareTo then returns positive`() {
        val later = buildEntity("2026-06-20T15:00:00")
        val earlier = buildEntity("2026-01-15T10:00:00")

        val result = later.compareTo(earlier)

        assertTrue(result > 0)
    }

    // ── Legacy format (yyyy-MM-dd) ─────────────────────────────────────

    @Test
    fun `given earlier legacy date when compareTo then returns negative`() {
        val earlier = buildEntity("2026-01-10")
        val later = buildEntity("2026-03-20")

        val result = earlier.compareTo(later)

        assertTrue(result < 0)
    }

    @Test
    fun `given same legacy date when compareTo then returns zero`() {
        val date1 = buildEntity("2026-04-15")
        val date2 = buildEntity("2026-04-15")

        val result = date1.compareTo(date2)

        assertEquals(0, result)
    }

    @Test
    fun `given later legacy date when compareTo then returns positive`() {
        val later = buildEntity("2026-12-31")
        val earlier = buildEntity("2026-01-01")

        val result = later.compareTo(earlier)

        assertTrue(result > 0)
    }
}
