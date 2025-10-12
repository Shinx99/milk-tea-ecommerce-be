package com.asm.ecommerce.shared;

import com.asm.ecommerce.shared.util.DateTimeUtil;
import com.asm.ecommerce.shared.util.StringUtil;
import com.asm.ecommerce.shared.util.ValidationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/*

# Run tất cả tests
mvn test

# Run chỉ TestSharedController
mvn test -Dtest=TestSharedController

# Run với output chi tiết
mvn test -Dtest=TestSharedController -X



[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.asm.ecommerce.shared.TestSharedController
[INFO] Tests run: 35, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.154 s - in TestSharedController
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 35, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] BUILD SUCCESS

*/



@SpringBootTest
@DisplayName("Shared Utilities Tests")
class TestSharedController {

    // ============================================
    // DateTimeUtil Tests
    // ============================================

    @Test
    @DisplayName("DateTimeUtil: Should format LocalDateTime to date string (yyyy-MM-dd)")
    void testFormatDate() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 10, 11, 15, 30, 45);
        String result = DateTimeUtil.formatDate(dateTime);

        assertNotNull(result);
        assertEquals("2025-10-11", result);
    }

    @Test
    @DisplayName("DateTimeUtil: Should format LocalDate to date string")
    void testFormatLocalDate() {
        LocalDate date = LocalDate.of(2025, 10, 11);
        String result = DateTimeUtil.formatDate(date);

        assertNotNull(result);
        assertEquals("2025-10-11", result);
    }

    @Test
    @DisplayName("DateTimeUtil: Should format LocalDateTime to datetime string (yyyy-MM-dd HH:mm:ss)")
    void testFormatDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 10, 11, 15, 30, 45);
        String result = DateTimeUtil.formatDateTime(dateTime);

        assertNotNull(result);
        assertEquals("2025-10-11 15:30:45", result);
    }

    @Test
    @DisplayName("DateTimeUtil: Should parse date string to LocalDate")
    void testParseDate() {
        LocalDate result = DateTimeUtil.parseDate("2025-10-11");

        assertNotNull(result);
        assertEquals(2025, result.getYear());
        assertEquals(10, result.getMonthValue());
        assertEquals(11, result.getDayOfMonth());
    }

    @Test
    @DisplayName("DateTimeUtil: Should throw exception for invalid date format")
    void testParseDateInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            DateTimeUtil.parseDate("invalid-date");
        });
    }

    @Test
    @DisplayName("DateTimeUtil: Should return null for null input in formatDate")
    void testFormatDateNull() {
        assertNull(DateTimeUtil.formatDate((LocalDateTime) null));
        assertNull(DateTimeUtil.formatDate((LocalDate) null));
    }

    @Test
    @DisplayName("DateTimeUtil: Should return null for null/empty string in parseDate")
    void testParseDateNull() {
        assertNull(DateTimeUtil.parseDate(null));
        assertNull(DateTimeUtil.parseDate(""));
        assertNull(DateTimeUtil.parseDate("   "));
    }

    @Test
    @DisplayName("DateTimeUtil: Should check if date is in the past")
    void testIsPast() {
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        assertTrue(DateTimeUtil.isPast(pastDate));
        assertFalse(DateTimeUtil.isPast(futureDate));
        assertFalse(DateTimeUtil.isPast(null));
    }

    @Test
    @DisplayName("DateTimeUtil: Should check if date is in the future")
    void testIsFuture() {
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        assertFalse(DateTimeUtil.isFuture(pastDate));
        assertTrue(DateTimeUtil.isFuture(futureDate));
        assertFalse(DateTimeUtil.isFuture(null));
    }

    @Test
    @DisplayName("DateTimeUtil: Should add days correctly")
    void testAddDays() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime future = DateTimeUtil.addDays(now, 5);

        assertNotNull(future);
        assertEquals(5, java.time.temporal.ChronoUnit.DAYS.between(now.toLocalDate(), future.toLocalDate()));
    }

    @Test
    @DisplayName("DateTimeUtil: Should add hours correctly")
    void testAddHours() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime future = DateTimeUtil.addHours(now, 3);

        assertNotNull(future);
        assertEquals(3, java.time.temporal.ChronoUnit.HOURS.between(now, future));
    }

    @Test
    @DisplayName("DateTimeUtil: Should check if date is between two dates")
    void testIsBetween() {
        LocalDateTime start = LocalDateTime.of(2025, 10, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 10, 31, 23, 59);
        LocalDateTime middle = LocalDateTime.of(2025, 10, 15, 12, 0);
        LocalDateTime before = LocalDateTime.of(2025, 9, 15, 12, 0);
        LocalDateTime after = LocalDateTime.of(2025, 11, 15, 12, 0);

        assertTrue(DateTimeUtil.isBetween(middle, start, end));
        assertTrue(DateTimeUtil.isBetween(start, start, end)); // Start inclusive
        assertTrue(DateTimeUtil.isBetween(end, start, end));   // End inclusive
        assertFalse(DateTimeUtil.isBetween(before, start, end));
        assertFalse(DateTimeUtil.isBetween(after, start, end));
    }

    // ============================================
    // ValidationUtil Tests
    // ============================================

    @ParameterizedTest
    @ValueSource(strings = {
            "test@gmail.com",
            "user.name@example.com",
            "test123@test.co.vn",
            "user+tag@example.com"
    })
    @DisplayName("ValidationUtil: Should validate correct email formats")
    void testValidEmails(String email) {
        assertTrue(ValidationUtil.isValidEmail(email),
                "Email should be valid: " + email);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "invalid-email",
            "@example.com",
            "test@",
            "test @example.com",
            "",
            "test.example.com"
    })
    @DisplayName("ValidationUtil: Should reject invalid email formats")
    void testInvalidEmails(String email) {
        assertFalse(ValidationUtil.isValidEmail(email),
                "Email should be invalid: " + email);
    }

    @Test
    @DisplayName("ValidationUtil: Should reject null email")
    void testNullEmail() {
        assertFalse(ValidationUtil.isValidEmail(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "0912345678",
            "0987654321",
            "01234567890"
    })
    @DisplayName("ValidationUtil: Should validate correct Vietnam phone numbers")
    void testValidPhones(String phone) {
        assertTrue(ValidationUtil.isValidPhone(phone),
                "Phone should be valid: " + phone);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "123456",           // Too short
            "1234567890",       // Doesn't start with 0
            "091234567890",     // Too long
            "09abcdefgh",       // Contains letters
            "0912 345 678"      // Contains spaces
    })
    @DisplayName("ValidationUtil: Should reject invalid phone numbers")
    void testInvalidPhones(String phone) {
        assertFalse(ValidationUtil.isValidPhone(phone),
                "Phone should be invalid: " + phone);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "password123",
            "Test1234",
            "MyPass99",
            "Abc12345678"
    })
    @DisplayName("ValidationUtil: Should validate strong passwords")
    void testValidPasswords(String password) {
        assertTrue(ValidationUtil.isValidPassword(password),
                "Password should be valid: " + password);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "short1",           // Too short
            "nodigits",         // No digits
            "12345678",         // No letters
            ""                  // Empty
    })
    @DisplayName("ValidationUtil: Should reject weak passwords")
    void testInvalidPasswords(String password) {
        assertFalse(ValidationUtil.isValidPassword(password),
                "Password should be invalid: " + password);
    }

    @ParameterizedTest
    @CsvSource({
            "1, true",
            "50, true",
            "999, true",
            "0, false",
            "-1, false",
            "1000, false"
    })
    @DisplayName("ValidationUtil: Should validate quantity within range")
    void testValidQuantity(int quantity, boolean expected) {
        assertEquals(expected, ValidationUtil.isValidQuantity(quantity),
                "Quantity " + quantity + " should be " + (expected ? "valid" : "invalid"));
    }

    @ParameterizedTest
    @CsvSource({
            "100.5, true",
            "0.01, true",
            "0, false",
            "-10, false"
    })
    @DisplayName("ValidationUtil: Should validate price is positive")
    void testValidPrice(double price, boolean expected) {
        assertEquals(expected, ValidationUtil.isValidPrice(price),
                "Price " + price + " should be " + (expected ? "valid" : "invalid"));
    }

    @Test
    @DisplayName("ValidationUtil: Should check null or empty strings")
    void testNullOrEmpty() {
        assertTrue(ValidationUtil.isNullOrEmpty(""));
        assertTrue(ValidationUtil.isNullOrEmpty(""));
        assertTrue(ValidationUtil.isNullOrEmpty("   "));
        assertFalse(ValidationUtil.isNullOrEmpty("test"));
        assertFalse(ValidationUtil.isNullOrEmpty(" test "));
    }

    @Test
    @DisplayName("ValidationUtil: Should check not null or empty")
    void testIsNotNullOrEmpty() {
        assertFalse(ValidationUtil.isNotNullOrEmpty(""));
        assertFalse(ValidationUtil.isNotNullOrEmpty(""));
        assertFalse(ValidationUtil.isNotNullOrEmpty("   "));
        assertTrue(ValidationUtil.isNotNullOrEmpty("test"));
    }

    // ============================================
    // StringUtil Tests
    // ============================================

    @ParameterizedTest
    @CsvSource({
            "'Áo thun nam đẹp', 'ao-thun-nam-dep'",
            "'Giày Nike Air Max', 'giay-nike-air-max'",
            "'iPhone 15 Pro Max', 'iphone-15-pro-max'",
            "'Test  Multiple   Spaces', 'test-multiple-spaces'"
    })
    @DisplayName("StringUtil: Should convert Vietnamese text to slug")
    void testToSlug(String input, String expected) {
        String result = StringUtil.toSlug(input);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("StringUtil: Should return empty string for null slug")
    void testToSlugNull() {
        assertEquals("", StringUtil.toSlug(null));
        assertEquals("", StringUtil.toSlug(""));
    }

    @ParameterizedTest
    @CsvSource({
            "'hello', 'Hello'",
            "'WORLD', 'World'",
            "'tEsT', 'Test'"
    })
    @DisplayName("StringUtil: Should capitalize first letter")
    void testCapitalize(String input, String expected) {
        assertEquals(expected, StringUtil.capitalize(input));
    }

    @ParameterizedTest
    @CsvSource({
            "'hello world', 'Hello World'",
            "'test STRING util', 'Test String Util'",
            "'one two three', 'One Two Three'"
    })
    @DisplayName("StringUtil: Should capitalize first letter of each word")
    void testCapitalizeWords(String input, String expected) {
        assertEquals(expected, StringUtil.capitalizeWords(input));
    }

    @Test
    @DisplayName("StringUtil: Should truncate long string with ellipsis")
    void testTruncate() {
        String longText = "This is a very long text that needs to be truncated";
        String result = StringUtil.truncate(longText, 20);

        assertEquals("This is a very lo...", result);
        assertEquals(20, result.length());
    }

    @Test
    @DisplayName("StringUtil: Should not truncate short string")
    void testTruncateShortString() {
        String shortText = "Short";
        String result = StringUtil.truncate(shortText, 20);
        assertEquals(shortText, result);
    }

    @Test
    @DisplayName("StringUtil: Should mask email correctly")
    void testMaskEmail() {
        assertEquals("t***t@gmail.com", StringUtil.maskEmail("test@gmail.com"));
        assertEquals("u***e@example.com", StringUtil.maskEmail("username@example.com"));
    }

    @Test
    @DisplayName("StringUtil: Should not mask short email username")
    void testMaskShortEmail() {
        String shortEmail = "ab@test.com";
        assertEquals(shortEmail, StringUtil.maskEmail(shortEmail));
    }

    @Test
    @DisplayName("StringUtil: Should mask phone correctly")
    void testMaskPhone() {
        assertEquals("091***5678", StringUtil.maskPhone("0912345678"));
        assertEquals("098***4321", StringUtil.maskPhone("0987654321"));
    }

    @Test
    @DisplayName("StringUtil: Should not mask short phone")
    void testMaskShortPhone() {
        String shortPhone = "123456";
        assertEquals(shortPhone, StringUtil.maskPhone(shortPhone));
    }

    @Test
    @DisplayName("StringUtil: Should generate random string with correct length")
    void testGenerateRandomString() {
        String random10 = StringUtil.generateRandomString(10);
        String random20 = StringUtil.generateRandomString(20);

        assertNotNull(random10);
        assertEquals(10, random10.length());

        assertNotNull(random20);
        assertEquals(20, random20.length());

        // Should generate different strings
        assertNotEquals(random10, random20);
    }

    @Test
    @DisplayName("StringUtil: Should format price with thousand separator")
    void testFormatPrice() {
        assertEquals("1,234,567", StringUtil.formatPrice(1234567));
        assertEquals("100", StringUtil.formatPrice(100));
        assertEquals("1,000,000", StringUtil.formatPrice(1000000));
    }

    @Test
    @DisplayName("StringUtil: Should remove Vietnamese accents")
    void testRemoveAccents() {
        assertEquals("Ha Noi", StringUtil.removeAccents("Hà Nội"));
        assertEquals("Viet Nam", StringUtil.removeAccents("Việt Nam"));
        assertEquals("Ao thun dep", StringUtil.removeAccents("Áo thun đẹp"));
    }

    @Test
    @DisplayName("StringUtil: Should sanitize string")
    void testSanitize() {
        assertEquals("test", ValidationUtil.sanitize("  test  "));
        assertEquals("", ValidationUtil.sanitize(null));
        assertEquals("", ValidationUtil.sanitize(""));
    }
}
