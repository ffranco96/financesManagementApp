package com.example.financesmanagementapp.ui

import java.io.Serializable

data class Record (
    var amount: Double = 0.0,
    var title: String = "",
    var description: String = "",
    var isIncome: Boolean = false,
    //var category: Category = Category(),
    var date:String = "",
    var currency:String = ""
): Serializable