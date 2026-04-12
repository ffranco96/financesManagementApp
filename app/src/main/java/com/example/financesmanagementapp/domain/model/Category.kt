package com.example.financesmanagementapp.domain.model

import com.example.financesmanagementapp.R

/**
 * Data class representing the category of a movement in the app.
 * The categories can be: Food, Health, Hobbies, Investments, etc.
 */
data class Category(
    var categoryName: String = "Otro",
    var iconRsc: Int = R.drawable.ic_other_generic,
    var colorIcon: Int = R.color.sad_grey,
    var details: String = ""
)