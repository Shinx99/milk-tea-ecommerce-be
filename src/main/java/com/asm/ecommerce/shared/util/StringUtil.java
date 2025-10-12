package com.asm.ecommerce.shared.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public final class StringUtil {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern EDGESDHASHES = Pattern.compile("(^-|-$)");

    /**
     * Convert string to slug (URL-friendly)
     * Example: "Áo thun nam" -> "ao-thun-nam"
     */
    public static String toSlug(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        slug = EDGESDHASHES.matcher(slug).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    /**
     * Capitalize first letter
     */
    public static String capitalize(String str) {
        if (ValidationUtil.isNullOrEmpty(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * Capitalize first letter of each word
     */
    public static String capitalizeWords(String str) {
        if (ValidationUtil.isNullOrEmpty(str)) {
            return str;
        }

        String[] words = str.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(capitalize(word)).append(" ");
            }
        }

        return result.toString().trim();
    }

    /**
     * Truncate string to specified length with ellipsis
     */
    public static String truncate(String str, int maxLength) {
        if (str == null || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }

    /**
     * Mask email: example@gmail.com -> e***e@gmail.com
     */
    public static String maskEmail(String email) {
        if (ValidationUtil.isNullOrEmpty(email) || !email.contains("@")) {
            return email;
        }

        String[] parts = email.split("@");
        String username = parts[0];
        String domain = parts[1];

        if (username.length() <= 2) {
            return email;
        }

        String masked = username.charAt(0) +
                "*".repeat(username.length() - 2) +
                username.charAt(username.length() - 1);

        return masked + "@" + domain;
    }

    /**
     * Mask phone: 0912345678 -> 091***5678
     */
    public static String maskPhone(String phone) {
        if (ValidationUtil.isNullOrEmpty(phone) || phone.length() < 7) {
            return phone;
        }

        int visibleStart = 3;
        int visibleEnd = 4;
        int maskLength = phone.length() - visibleStart - visibleEnd;

        return phone.substring(0, visibleStart) +
                "*".repeat(maskLength) +
                phone.substring(phone.length() - visibleEnd);
    }

    /**
     * Generate random string (alphanumeric)
     */
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }

    /**
     * Format price with thousand separator
     * Example: 1234567 -> "1,234,567"
     */
    public static String formatPrice(Number price) {
        if (price == null) {
            return "0";
        }
        return String.format("%,d", price.longValue());
    }

    /**
     * Remove Vietnamese accents
     * Example: "Hà Nội" -> "Ha Noi"
     */
    public static String removeAccents(String str) {
        if (ValidationUtil.isNullOrEmpty(str)) {
            return str;
        }

        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

    private StringUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
