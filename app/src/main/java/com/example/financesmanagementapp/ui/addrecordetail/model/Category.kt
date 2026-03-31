package com.example.financesmanagementapp.ui.addrecordetail.model
import com.example.financesmanagementapp.R

data class Category(
    var categoryName: String = "Otro",
    var iconRsc: Int = R.drawable.ic_other_generic,
    var colorIcon: Int = R.color.sad_grey,
    var details: String = ""
)
