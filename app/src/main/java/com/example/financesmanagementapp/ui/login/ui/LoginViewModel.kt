package com.example.financesmanagementapp.ui.login.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel(){
    private val _user = MutableStateFlow(String())
    val user: StateFlow<String> = _user

    private val _password = MutableStateFlow(String())
    val password: StateFlow<String> = _password

    private val _loginEnabled = MutableStateFlow(false)
    val loginEnabled: StateFlow<Boolean> = _loginEnabled

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onLoginDataChanged(user: String, password: String) {
        _user.value = user
        _password.value = password
        _loginEnabled.value = isValidUser(user) && isValidPassword(password)
    }

    private fun isValidUser(user: String): Boolean = user.length > 6
    private fun isValidPassword(password: String): Boolean = password.length > 6
    suspend fun onLoginButtonClicked(/*navegador?*/) {
        Log.d("franco", "Hola")
        // Actions to be done when the user presses to perform login
        _isLoading.value = true
        delay(2000) // It's a coroutine
        _isLoading.value = false
    }

}