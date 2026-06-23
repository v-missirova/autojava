package org.company;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class SoftAssertionsTests {
    @Test
    void shouldVerifyAccountPropertiesWithSoftAssertions() {
        Account account = new Account("101", new BigDecimal("500.50"));
        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(account.getId())
                .as("check id")
                .isEqualTo("101");

        softly.assertThat(account.getBalance())
                .as("check balance")
                .isEqualByComparingTo("500.50");

        softly.assertThat(account.getId())
                .startsWith("10");

        softly.assertAll();
    }
}