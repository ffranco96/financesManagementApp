package com.example.financesmanagementapp.ui.home.ui

/**
 * Ui event that can be triggered by the HomeStartScreen.
 */
sealed class HomeUiEvent {
    data class ShowToast(val message: String) : HomeUiEvent()
}