package com.boryans.covidstats.util


import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CountryValidationUtilTest {



    @Test
    fun `empty country input returns false`() {
        val result = CountryValidationUtil.validateCountryNameInput(
            " "
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `typo country input returns false`() {
        val result = CountryValidationUtil.validateCountryNameInput(
            "serbia"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `whitespaces in country input returns false`() {
        val result = CountryValidationUtil.validateCountryNameInput(
            " Serbia"
        )
        assertThat(result).isFalse()
    }
}