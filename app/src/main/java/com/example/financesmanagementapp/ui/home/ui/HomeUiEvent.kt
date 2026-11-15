package com.example.financesmanagementapp.ui.home.ui

/**
 * Ui event that can be triggered by the HomeStartScreen.
 */
sealed class HomeUiEvent {
    data class ShowToast(val message: String) : HomeUiEvent()

    /**
     * Asks the UI to open the system file picker so the user can choose where
     * the exported CSV document is written.
     *
     * @param suggestedFileName Default name proposed to the user.
     */
    data class LaunchCsvExport(val suggestedFileName: String) : HomeUiEvent()
}
