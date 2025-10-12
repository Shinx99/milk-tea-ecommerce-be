package com.asm.ecommerce.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ValidationUtil Tests")
class ValidationUtilTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "test@gmail.com",
            "user.name@example.com",
            "test123@test.co.vn"
    })
    @DisplayName("Should validate correct email formats")
    void testValidEmails(String email) {
        assertTrue(ValidationUtil.isValidEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "invalid-email",
            "@example.com",
            "test@",
            "test @example.com",
            ""
    })
    @DisplayName("Should reject invalid email formats")
    void testInvalidEmails(String email) {
        assertFalse(ValidationUtil.isValidEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "0912345678",
            "0987654321",
            "01234567890"
    })
    @DisplayName("Should validate correct Vietnam phone numbers")
    void testValidPhones(String phone) {
        assertTrue(ValidationUtil.isValidPhone(phone));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "123456",           // Too short
            "1234567890",       // Doesn't start with 0
            "091234567890",     // Too long
            "09abcdefgh"        // Contains letters
    })
    @DisplayName("Should reject invalid phone numbers")
    void testInvalidPhones(String phone) {
        assertFalse(ValidationUtil.isValidPhone(phone));
    }

    @Test
    @DisplayName("Should validate quantity within range")
    void testValidQuantity() {
        assertTrue(ValidationUtil.isValidQuantity(1));
        assertTrue(ValidationUtil.isValidQuantity(50));
        assertTrue(ValidationUtil.isValidQuantity(999));

        assertFalse(ValidationUtil.isValidQuantity(0));
        assertFalse(ValidationUtil.isValidQuantity(-1));
        assertFalse(ValidationUtil.isValidQuantity(1000));
    }

    @Test
    @DisplayName("Should check null or empty strings")
    void testNullOrEmpty() {
        assertTrue(ValidationUtil.isNullOrEmpty(""));
        assertTrue(ValidationUtil.isNullOrEmpty(""));
        assertTrue(ValidationUtil.isNullOrEmpty("   "));
        assertFalse(ValidationUtil.isNullOrEmpty("test"));
    }
}
