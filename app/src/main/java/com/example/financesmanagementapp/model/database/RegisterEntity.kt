package com.example.financesmanagementapp.model.database

data class RegisterEntity (
    var amount: Double = 0.0,
    var title: String = "",
    var description: String = "",
    var category: String /*Category = Category()*/,
    var date:String = "",
    var currency:String = "",
    //@PrimaryKey(autoGenerate = true) var id: Int = 0
)