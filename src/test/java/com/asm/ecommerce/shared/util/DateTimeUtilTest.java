package com.asm.ecommerce.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DateTimeUtil Tests")
class DateTimeUtilTest {

    @Test
    @DisplayName("Should format LocalDateTime to date string")
    void testFormatDate() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 10, 11, 15, 30);
        String result = DateTimeUtil.formatDate(dateTime);
        assertEquals("2025-10-11", result);
    }

    @Test
    @DisplayName("Should format LocalDateTime to datetime string")
    void testFormatDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 10, 11, 15, 30, 45);
        String result = DateTimeUtil.formatDateTime(dateTime);
        assertEquals("2025-10-11 15:30:45", result);
    }

    @Test
    @DisplayName("Should parse date string to LocalDate")
    void testParseDate() {
        LocalDate result = DateTimeUtil.parseDate("2025-10-11");
        assertEquals(2025, result.getYear());
        assertEquals(10, result.getMonthValue());
        assertEquals(11, result.getDayOfMonth());
    }

    @Test
    @DisplayName("Should throw exception for invalid date format")
    void testParseDateInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            DateTimeUtil.parseDate("invalid-date");
        });
    }

    @Test
    @DisplayName("Should check if date is in the past")
    void testIsPast() {
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        assertTrue(DateTimeUtil.isPast(pastDate));

        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        assertFalse(DateTimeUtil.isPast(futureDate));
    }

    @Test
    @DisplayName("Should add days correctly")
    void testAddDays() {
        LocalDateTime now = DateTimeUtil.now();
        LocalDateTime future = DateTimeUtil.addDays(now, 5);
        assertEquals(5, java.time.temporal.ChronoUnit.DAYS.between(now, future));
    }
}
