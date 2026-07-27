package com.example.financesmanagementapp.ui.addregisteramount.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AddRecordAmountScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun setContent(
        amountText: String = "",
        checkedSwitch: Boolean = false,
        expandedCurrencyMenu: Boolean = false,
        selectedCurrency: String = "",
        currencyList: List<String> = emptyList(),
        onAmountTextChanged: (String) -> Unit = {},
        onCheckedSwitchChange: (Boolean) -> Unit = {},
        onDropDownClick: () -> Unit = {},
        onDismissRequest: () -> Unit = {},
        onCurrencySelected: (String) -> Unit = {},
        onBackClick: () -> Unit = {},
        onNextClick: () -> Unit = {},
    ) {
        composeRule.setContent {
            AddRecordAmountContent(
                amountText = amountText,
                checkedSwitch = checkedSwitch,
                expandedCurrencyMenu = expandedCurrencyMenu,
                selectedCurrency = selectedCurrency,
                currencyList = currencyList,
                onAmountTextChanged = onAmountTextChanged,
                onCheckedSwitchChange = onCheckedSwitchChange,
                onDropDownClick = onDropDownClick,
                onDismissRequest = onDismissRequest,
                onCurrencySelected = onCurrencySelected,
                onBackClick = onBackClick,
                onNextClick = onNextClick,
            )
        }
    }

    @Test
    fun givenAmountTextField_whenInputTextInAmountTextField_thenOnAmountTextChangedIsCalled() {
        // given
        var typed = ""
        setContent(onAmountTextChanged = { typed = it })

        // when
        composeRule.onNodeWithTag(AMOUNT_INPUT).performTextInput("234")

        // then
        assertEquals("234", typed)
    }
}
