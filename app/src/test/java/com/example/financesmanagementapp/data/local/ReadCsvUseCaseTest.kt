package com.example.financesmanagementapp.data.local

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.example.financesmanagementapp.domain.crash.CrashReporter
import io.mockk.every
import io.mockk.mockk
import okio.FileNotFoundException
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.InputStream

class ReadCsvUseCaseTest {
    private val context: Context = mockk()
    private val contentResolver: ContentResolver = mockk()
    private val uri: Uri = mockk()
    private val crashReporter: CrashReporter = mockk(relaxed = true)
    private lateinit var readCsvUseCase: ReadCsvUseCase
    private val inputStream: InputStream = mockk()

    @Before
    fun setUp() {
        readCsvUseCase = ReadCsvUseCase(context, crashReporter)
        every { context.contentResolver } returns contentResolver
        every { uri.authority } returns "com.test.provider"
    }

    @Test
    fun `given incorrect URI when invoke then returns null`() {
        // Given
        every { contentResolver.openInputStream(uri) } returns null

        // When
        val result = readCsvUseCase.invoke(uri)

        //Then
        assertNull(result)
    }

    @Test
    fun `given correct URI when invoke then returns valid input stream`() {
        // Given
        every { contentResolver.openInputStream(uri) } returns inputStream

        // When
        val result = readCsvUseCase.invoke(uri)

        // Then
        assertNotNull(result)
        assertEquals(inputStream, result)
    }

    @Test
    fun `given content resolver throws exception when invoke then returns null`() {
        // Given
        every { contentResolver.openInputStream(uri) } throws FileNotFoundException()

        // When
        val result = readCsvUseCase.invoke(uri)

        //Then
        assertNull(result)
    }

}