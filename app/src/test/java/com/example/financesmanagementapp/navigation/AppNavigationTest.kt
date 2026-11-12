package com.example.financesmanagementapp.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

class AppNavigationTest {

    @Test
    fun `login disabled skips login screen regardless of isLoggedIn`() {
        assertEquals(
            AppScreens.HomeStartScreen.route,
            resolveStartDestination(loginEnabled = false, isLoggedIn = false)
        )
        assertEquals(
            AppScreens.HomeStartScreen.route,
            resolveStartDestination(loginEnabled = false, isLoggedIn = true)
        )
    }

    @Test
    fun `login enabled and not logged in shows login screen`() {
        assertEquals(
            AppScreens.LoginScreen.route,
            resolveStartDestination(loginEnabled = true, isLoggedIn = false)
        )
    }

    @Test
    fun `login enabled and logged in shows home screen`() {
        assertEquals(
            AppScreens.HomeStartScreen.route,
            resolveStartDestination(loginEnabled = true, isLoggedIn = true)
        )
    }
}
