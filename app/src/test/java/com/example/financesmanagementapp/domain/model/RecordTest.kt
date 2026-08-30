package com.example.financesmanagementapp.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RecordTest {

    private fun buildRecord(date: String) = Record(date = date)

    // ── LocalDateTime format (yyyy-MM-dd'T'HH:mm:ss) ──────────────────

    @Test
    fun `given earlier date when compareTo then returns negative`() {
        val earlier = buildRecord("2026-01-15T10:00:00")
        val later = buildRecord("2026-01-15T12:00:00")

        val result = earlier.compareTo(later)

        assertTrue(result < 0)
    }

    @Test
    fun `given same datetime when compareTo then returns zero`() {
        val date1 = buildRecord("2026-01-15T10:30:00")
        val date2 = buildRecord("2026-01-15T10:30:00")

        val result = date1.compareTo(date2)

        assertEquals(0, result)
    }

    @Test
    fun `given later date when compareTo then returns positive`() {
        val later = buildRecord("2026-06-20T15:00:00")
        val earlier = buildRecord("2026-01-15T10:00:00")

        val result = later.compareTo(earlier)

        assertTrue(result > 0)
    }

    // ── Legacy format (yyyy-MM-dd) ─────────────────────────────────────

    @Test
    fun `given earlier legacy date when compareTo then returns negative`() {
        val earlier = buildRecord("2026-01-10")
        val later = buildRecord("2026-03-20")

        val result = earlier.compareTo(later)

        assertTrue(result < 0)
    }

    @Test
    fun `given same legacy date when compareTo then returns zero`() {
        val date1 = buildRecord("2026-04-15")
        val date2 = buildRecord("2026-04-15")

        val result = date1.compareTo(date2)

        assertEquals(0, result)
    }

    @Test
    fun `given later legacy date when compareTo then returns positive`() {
        val later = buildRecord("2026-12-31")
        val earlier = buildRecord("2026-01-01")

        val result = later.compareTo(earlier)

        assertTrue(result > 0)
    }

    // ── Fallback (unparseable as LocalDateTime or LocalDate: compared as string) ──

    @Test
    fun `given unparseable dates when compareTo then falls back to string comparison and returns negative`() {
        val earlier = buildRecord("15-01-2026")
        val later = buildRecord("20-01-2026")

        val result = earlier.compareTo(later)

        assertTrue(result < 0)
    }

    @Test
    fun `given same unparseable date when compareTo then falls back to string comparison and returns zero`() {
        val date1 = buildRecord("not-a-date")
        val date2 = buildRecord("not-a-date")

        val result = date1.compareTo(date2)

        assertEquals(0, result)
    }

    @Test
    fun `given unparseable dates when compareTo then falls back to string comparison and returns positive`() {
        val later = buildRecord("20-01-2026")
        val earlier = buildRecord("15-01-2026")

        val result = later.compareTo(earlier)

        assertTrue(result > 0)
    }

    @Test
    fun `given blank dates when compareTo then falls back to string comparison without throwing`() {
        val date1 = buildRecord("")
        val date2 = buildRecord("")

        val result = date1.compareTo(date2)

        assertEquals(0, result)
    }
}
