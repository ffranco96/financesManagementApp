package com.example.financesmanagementapp.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import javax.inject.Inject

// TODO crear la parte de seleccion desde aca adentro o desde afuera del use case, desde la UI
class SelectCsvUseCase @Inject constructor(
    @ApplicationContext private val context: Context
){
    suspend operator fun invoke(): InputStream{
        return context.assets.open("records.csv")
    }
}