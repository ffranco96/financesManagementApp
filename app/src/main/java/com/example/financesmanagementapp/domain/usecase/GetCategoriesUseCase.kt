package com.example.financesmanagementapp.domain.usecase

import com.example.financesmanagementapp.data.repository.ConfigRepository
import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.CategoryName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.Collator
import java.util.Locale
import javax.inject.Inject

/**
 * Provides the categories shown in the category selector.
 *
 * Categories are persisted as a DataStore `Set`, which does not preserve insertion order, so the
 * list is sorted here: alphabetically by parent category label and, within each parent, by
 * subcategory label. Spanish collation is used so accented labels sort as their base letters.
 * [CategoryName.WITHOUT_CATEGORY] is kept at the end as the placeholder entry.
 */
class GetCategoriesUseCase @Inject constructor(
    private val repository: ConfigRepository
) {
    operator fun invoke(): Flow<List<Category>> = repository.getCategories()
        .map { categories -> categories.sortedForDisplay() }

    private fun List<Category>.sortedForDisplay(): List<Category> {
        val textOrder = spanishTextOrder()
        val (withoutCategory, selectable) =
            partition { it.categoryName == CategoryName.WITHOUT_CATEGORY }
        return selectable.sortedWith(
            compareBy<Category, String>(textOrder) { it.categoryName.label }
                .thenBy(textOrder) { it.subcategoryName.label }
        ) + withoutCategory
    }
}

/**
 * Spanish-aware comparator for UI labels, so accented letters sort next to their base letter
 * (e.g. "Ámbar" before "Zapato", "Alba" before "Ámbar") instead of after "Z" as raw UTF-16 order
 * would do. A fresh [Collator] is created per call because it is not thread-safe.
 */
internal fun spanishTextOrder(): Comparator<String> {
    val collator = Collator.getInstance(Locale.forLanguageTag("es-AR"))
    return Comparator { first, second -> collator.compare(first, second) }
}
