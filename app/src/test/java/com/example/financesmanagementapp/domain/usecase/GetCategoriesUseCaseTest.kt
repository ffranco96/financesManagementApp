package com.example.financesmanagementapp.domain.usecase

import com.example.financesmanagementapp.data.repository.ConfigRepository
import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.CategoryName
import com.example.financesmanagementapp.domain.model.SubcategoryName
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetCategoriesUseCaseTest {

    private val mockRepository: ConfigRepository = mockk()
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase

    @Before
    fun setUp() {
        getCategoriesUseCase = GetCategoriesUseCase(mockRepository)
    }

    @Test
    fun `given unsorted categories when invoke then sorts by category and subcategory label`() = runTest {
        val categories = listOf(
            Category(CategoryName.CATEGORY_TRAVEL, SubcategoryName.SUBCATEGORY_FOOD),
            Category(CategoryName.CATEGORY_HEALTH, SubcategoryName.SUBCATEGORY_MEDICINE),
            Category(CategoryName.CATEGORY_TRAVEL, SubcategoryName.SUBCATEGORY_HOTELS),
            Category(CategoryName.CATEGORY_HEALTH, SubcategoryName.SUBCATEGORY_BEAUTY)
        )
        every { mockRepository.getCategories() } returns flowOf(categories)

        val result = getCategoriesUseCase().first()

        assertEquals(
            listOf(
                CategoryName.CATEGORY_HEALTH to SubcategoryName.SUBCATEGORY_BEAUTY,
                CategoryName.CATEGORY_HEALTH to SubcategoryName.SUBCATEGORY_MEDICINE,
                CategoryName.CATEGORY_TRAVEL to SubcategoryName.SUBCATEGORY_FOOD,
                CategoryName.CATEGORY_TRAVEL to SubcategoryName.SUBCATEGORY_HOTELS
            ),
            result.map { it.categoryName to it.subcategoryName }
        )
    }

    @Test
    fun `given accented labels when comparing with spanish text order then sorts them by base letter`() {
        val textOrder = spanishTextOrder()

        // Raw UTF-16 order would place "Ámbar" after "Zapato" ('Á' > 'Z').
        assertTrue(textOrder.compare("Ámbar", "Zapato") < 0)
        // Accents are a tertiary difference: "Alba" comes before "Ámbar".
        assertTrue(textOrder.compare("Alba", "Ámbar") < 0)
        assertTrue(textOrder.compare("Ámbar", "Alba") > 0)
    }

    @Test
    fun `given categories with accented labels when invoke then sorts by category label`() = runTest {
        val categories = listOf(
            Category(CategoryName.CATEGORY_TECHNOLOGY, SubcategoryName.SUBCATEGORY_SOFTWARE_AND_GAMES),
            Category(CategoryName.CATEGORY_EDUCATION_AND_DEVELOPMENT, SubcategoryName.SUBCATEGORY_OTHERS),
            Category(CategoryName.CATEGORY_VEHICLE, SubcategoryName.SUBCATEGORY_FUEL)
        )
        every { mockRepository.getCategories() } returns flowOf(categories)

        val result = getCategoriesUseCase().first()

        assertEquals(
            listOf(
                CategoryName.CATEGORY_EDUCATION_AND_DEVELOPMENT,
                CategoryName.CATEGORY_TECHNOLOGY,
                CategoryName.CATEGORY_VEHICLE
            ),
            result.map { it.categoryName }
        )
    }

    @Test
    fun `given without category when invoke then keeps it at the end of the list`() = runTest {
        val categories = listOf(
            Category(CategoryName.WITHOUT_CATEGORY, SubcategoryName.SUBCATEGORY_NONE),
            Category(CategoryName.CATEGORY_TECHNOLOGY, SubcategoryName.SUBCATEGORY_SOFTWARE_AND_GAMES),
            Category(CategoryName.CATEGORY_HEALTH, SubcategoryName.SUBCATEGORY_MEDICINE)
        )
        every { mockRepository.getCategories() } returns flowOf(categories)

        val result = getCategoriesUseCase().first()

        // "Sin categoría" would sort between "Salud" and "Tecnología" if treated alphabetically.
        assertEquals(
            listOf(
                CategoryName.CATEGORY_HEALTH,
                CategoryName.CATEGORY_TECHNOLOGY,
                CategoryName.WITHOUT_CATEGORY
            ),
            result.map { it.categoryName }
        )
    }

    @Test
    fun `given categories sharing a subcategory when invoke then keeps one entry per parent category`() = runTest {
        val categories = listOf(
            Category(CategoryName.CATEGORY_VEHICLE, SubcategoryName.SUBCATEGORY_INSURANCE),
            Category(CategoryName.CATEGORY_TRAVEL, SubcategoryName.SUBCATEGORY_INSURANCE),
            Category(CategoryName.CATEGORY_HOUSE_AND_HOME, SubcategoryName.SUBCATEGORY_INSURANCE)
        )
        every { mockRepository.getCategories() } returns flowOf(categories)

        val result = getCategoriesUseCase().first()

        assertEquals(3, result.size)
        assertEquals(
            listOf(
                CategoryName.CATEGORY_HOUSE_AND_HOME,
                CategoryName.CATEGORY_VEHICLE,
                CategoryName.CATEGORY_TRAVEL
            ),
            result.map { it.categoryName }
        )
    }
}
