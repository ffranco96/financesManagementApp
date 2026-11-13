package com.example.financesmanagementapp.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

class AppNavigationTest {

    @Test
    fun `login disabled skips login screen and starts at home`() {
        assertEquals(
            AppScreens.HomeStartScreen.route,
            resolveStartDestination(loginEnabled = false)
        )
    }

    @Test
    fun `login enabled starts at login screen`() {
        assertEquals(
            AppScreens.LoginScreen.route,
            resolveStartDestination(loginEnabled = true)
        )
    }
}
