package com.example.financesmanagementapp.ui

import com.example.financesmanagementapp.data.local.entities.RecordEntity
import com.example.financesmanagementapp.domain.model.Category
import java.io.Serializable

/**
 * Data class representing a financial record, which is a movement that can be an income or an expense.
 */
data class Record (
    var amount: Double = 0.0,
    var description: String = "",
    var isIncome: Boolean = false,
    var category: Category = Category(),
    var date:String = "",
    var currency:String = ""
): Serializable

/** @improvement Acá la capa de dominio conoce a la de Datos, lo cual crea una dependencia
 hacia abajo. Puede mejorarse con un mapper.*/
fun Record.toEntity(): RecordEntity{
    return RecordEntity(
        amount = amount,
        description = description,
        isIncome = isIncome,
        categoryName = category.categoryName,
        date = date,
        currency = currency
    )
}
