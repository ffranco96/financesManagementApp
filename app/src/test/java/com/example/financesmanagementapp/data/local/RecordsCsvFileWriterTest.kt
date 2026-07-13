package com.example.financesmanagementapp.data.local

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.nio.file.Files

class RecordsCsvFileWriterTest {

    private lateinit var mockContext: Context
    private lateinit var mockContentResolver: ContentResolver
    private lateinit var testDir: File
    private lateinit var writer: RecordsCsvFileWriter

    @Before
    fun setUp() {
        testDir = Files.createTempDirectory("csvTest").toFile()
        mockContext = mockk()
        mockContentResolver = mockk()
        every { mockContext.filesDir } returns testDir
        every { mockContext.contentResolver } returns mockContentResolver

        mockkStatic(Uri::class)
        every { Uri.fromFile(any()) } returns mockk()

        writer = RecordsCsvFileWriter(mockContext)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `given openOutputStream returns null when writeCsv then returns false`() {
        every { mockContentResolver.openOutputStream(any()) } returns null

        val result = writer.writeCsv("content", "test.csv")

        assertFalse(result)
        assertFalse(File(testDir, "test.csv").exists())
    }

    @Test(expected = IOException::class)
    fun `given write throws IOException when writeCsv then throws IOException`() {
        val mockOutputStream = mockk<OutputStream>(relaxed = true)
        every { mockOutputStream.write(any<ByteArray>(), any<Int>(), any<Int>()) } throws IOException("Simulated IO error")
        every { mockContentResolver.openOutputStream(any()) } returns mockOutputStream

        writer.writeCsv("content", "test.csv")
    }
}
