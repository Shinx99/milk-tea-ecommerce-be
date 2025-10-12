package com.asm.ecommerce.shared.util;

import com.asm.ecommerce.shared.constant.AppConstants;

import java.util.Collection;
import java.util.regex.Pattern;

public final class ValidationUtil {

    // Email regex pattern (RFC 5322 simplified)
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    // Phone pattern (Vietnam: 10-11 digits, starting with 0)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^0[0-9]{9,10}$"
    );

    // Password pattern (at least 1 letter, 1 number, 8+ characters)
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$"
    );

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) {
            return false;
        }
        return email.length() <= AppConstants.MAX_EMAIL_LENGTH &&
                EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validate phone number (Vietnam format)
     */
    public static boolean isValidPhone(String phone) {
        if (isNullOrEmpty(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Validate password strength
     * At least 8 characters, 1 letter and 1 number
     */
    public static boolean isValidPassword(String password) {
        if (isNullOrEmpty(password)) {
            return false;
        }
        return password.length() >= AppConstants.MIN_PASSWORD_LENGTH &&
                password.length() <= AppConstants.MAX_PASSWORD_LENGTH &&
                PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Check if string is null or empty/blank
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Check if string is not null and not empty
     */
    public static boolean isNotNullOrEmpty(String str) {
        return !isNullOrEmpty(str);
    }

    /**
     * Check if collection is null or empty
     */
    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Check if collection is not null and not empty
     */
    public static boolean isNotNullOrEmpty(Collection<?> collection) {
        return !isNullOrEmpty(collection);
    }

    /**
     * Validate quantity is within allowed range
     */
    public static boolean isValidQuantity(int quantity) {
        return quantity >= AppConstants.MIN_ORDER_QUANTITY &&
                quantity <= AppConstants.MAX_ORDER_QUANTITY;
    }

    /**
     * Validate price is positive
     */
    public static boolean isValidPrice(Number price) {
        return price != null && price.doubleValue() > 0;
    }

    /**
     * Check if string length is within range
     */
    public static boolean isLengthValid(String str, int min, int max) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        return length >= min && length <= max;
    }

    /**
     * Sanitize string (remove leading/trailing spaces, null to empty)
     */
    public static String sanitize(String str) {
        return str == null ? "" : str.trim();
    }

    private ValidationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
