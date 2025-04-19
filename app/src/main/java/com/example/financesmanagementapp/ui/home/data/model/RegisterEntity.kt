package com.example.financesmanagementapp.ui.home.data.model

data class RegisterEntity ( // For now, it will be used as RegisterModel, too
    var amount: Double = 0.0,
    var title: String = "",
    var description: String = "",
    var category: String /*Category = Category()*/,
    var date:String = "",
    var currency:String = "",
    //@PrimaryKey(autoGenerate = true) var id: Int = 0
)