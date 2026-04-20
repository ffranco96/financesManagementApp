package com.example.financesmanagementapp.ui.home.domain

import com.example.financesmanagementapp.data.local.entities.toDomain
import com.example.financesmanagementapp.data.repository.ConfigRepository
import com.example.financesmanagementapp.data.repository.RecordsRepository
import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.Record
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Use case to retrieve all financial records as a Flow.
 *
 * @property recordsRepository The repository to fetch records from.
 */
class GetAllRecordsFlowUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val configRepository: ConfigRepository,
) {
    /**
     * Executes the use case to get the flow of all records as [Record] objects.
     * To complete the category that has been saved as a plain String, a combination is made
     * between the record entities and the categories obtained present in the datastore on run
     * time. The obtained category is saved on the 'category' attribute of the [Record] object.
     * In case the Category couldn't be found, tries to match with "Faltantes" category. At last, if
     * also Faltantes can't be found in datastore, assigns an Category empty, just with "Faltantes"
     * category name.
     *
     * @return A Flow emitting the list of records with the complete category as a [List<Record>].
     */
    operator fun invoke(): Flow<List<Record>> {
        // Combines the most recent values of each flow
        return combine(
            recordsRepository.getAllRecordsFlow(),
            configRepository.getCategories()
        ){ entities, categories ->
            entities.map{ recordEntity ->
                val completeCategory =
                    categories.find { it.categoryName == recordEntity.categoryName }
                        ?: categories.find { it.categoryName == "Faltantes" } ?: Category("Faltantes")
                recordEntity.toDomain(completeCategory)
            }
        }
    }
}
