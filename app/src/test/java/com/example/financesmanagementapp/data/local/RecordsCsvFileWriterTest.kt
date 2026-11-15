package com.example.financesmanagementapp.data.local

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream

class RecordsCsvFileWriterTest {

    private lateinit var mockContext: Context
    private lateinit var mockContentResolver: ContentResolver
    private lateinit var destination: Uri
    private lateinit var writer: RecordsCsvFileWriter

    @Before
    fun setUp() {
        destination = mockk()
        mockContentResolver = mockk()
        mockContext = mockk()
        every { mockContext.contentResolver } returns mockContentResolver

        writer = RecordsCsvFileWriter(mockContext)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `given writable document when writeCsv then writes content and returns true`() {
        val outputStream = ByteArrayOutputStream()
        every { mockContentResolver.openOutputStream(destination, "wt") } returns outputStream

        val result = writer.writeCsv(destination, "amount; description\n10.0; test")

        assertTrue(result)
        assertEquals("amount; description\n10.0; test", outputStream.toString("UTF-8"))
    }

    @Test
    fun `given document cannot be opened when writeCsv then returns false`() {
        every { mockContentResolver.openOutputStream(destination, "wt") } returns null

        val result = writer.writeCsv(destination, "content")

        assertFalse(result)
    }

    @Test(expected = IOException::class)
    fun `given write throws IOException when writeCsv then propagates`() {
        val mockOutputStream = mockk<OutputStream>(relaxed = true)
        every { mockOutputStream.write(any<ByteArray>(), any<Int>(), any<Int>()) } throws IOException("Simulated IO error")
        every { mockContentResolver.openOutputStream(destination, "wt") } returns mockOutputStream

        writer.writeCsv(destination, "content")
    }
}
