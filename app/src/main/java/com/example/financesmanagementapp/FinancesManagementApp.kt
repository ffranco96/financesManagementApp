package com.example.financesmanagementapp

import android.app.Application
import com.example.financesmanagementapp.domain.usecase.InitializeConfigUseCase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class FinancesManagementApp : Application() {

    @Inject
    lateinit var initializeConfigUseCase: InitializeConfigUseCase

    override fun onCreate() {
        super.onCreate()
        MainScope().launch {
            initializeConfigUseCase()
        }
    }
}
