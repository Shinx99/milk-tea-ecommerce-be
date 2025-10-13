package com.asm.ecommerce.shared.constant;

public class AppConstants {

    // API Configuration
    public static final String API_PREFIX = "/api";
    public static final String API_VERSION = "/v1";
    public static final String API_BASE_PATH = API_PREFIX + API_VERSION;

    // Pagination
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_BY = "createdAt";
    public static final String DEFAULT_SORT_DIRECTION = "DESC";

    // Date & Time
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_ZONE = "Asia/Ho_Chi_Minh";

    // File Upload
    public static final long MAX_FILE_SIZE_MB = 5;
    public static final long MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024;
    public static final String[] ALLOWED_IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".webp"};
    public static final String[] ALLOWED_IMAGE_MIME_TYPES = {
            "image/jpeg", "image/png", "image/gif", "image/webp"
    };

    // Validation
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 100;
    public static final int MIN_PHONE_LENGTH = 10;
    public static final int MAX_PHONE_LENGTH = 11;
    public static final int MAX_EMAIL_LENGTH = 255;
    public static final int MAX_NAME_LENGTH = 255;

    // Business Rules
    public static final int MIN_ORDER_QUANTITY = 1;
    public static final int MAX_ORDER_QUANTITY = 999;
    public static final int CART_EXPIRY_DAYS = 7;
    public static final int ORDER_CANCEL_TIMEOUT_HOURS = 24;

    // Cache TTL (seconds)
    public static final int CACHE_TTL_SHORT = 300;      // 5 minutes
    public static final int CACHE_TTL_MEDIUM = 1800;    // 30 minutes
    public static final int CACHE_TTL_LONG = 3600;      // 1 hour

    // Rate Limiting
    public static final int MAX_LOGIN_ATTEMPTS = 5;
    public static final int LOGIN_BLOCK_DURATION_MINUTES = 15;

    private AppConstants(){
        throw new UnsupportedOperationException("This is utility class and cannot be instantiated");
    }

}