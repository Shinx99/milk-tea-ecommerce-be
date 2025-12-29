package com.asm.ecommerce.shared.constant;

public class MessageConstants {

    // Generic Success Messages
    public static final String SUCCESS = "Thao tác thành công";
    public static final String CREATED_SUCCESS = "Tạo mới thành công";
    public static final String UPDATED_SUCCESS = "Cập nhật thành công";
    public static final String DELETED_SUCCESS = "Xóa thành công";
    public static final String OPERATION_SUCCESS = "Thực hiện thành công";

    // Generic Error Messages
    public static final String ERROR_OCCURRED = "Đã xảy ra lỗi";
    public static final String INTERNAL_SERVER_ERROR = "Lỗi hệ thống. Vui lòng thử lại sau";
    public static final String NOT_FOUND = "Không tìm thấy tài nguyên";
    public static final String ALREADY_EXISTS = "Tài nguyên đã tồn tại";
    public static final String INVALID_REQUEST = "Yêu cầu không hợp lệ";
    public static final String UNAUTHORIZED = "Chưa xác thực. Vui lòng đăng nhập";
    public static final String FORBIDDEN = "Bạn không có quyền truy cập";
    public static final String BAD_REQUEST = "Dữ liệu không hợp lệ";

    // Validation Messages
    public static final String FIELD_REQUIRED = "Trường này là bắt buộc";
    public static final String INVALID_EMAIL = "Email không hợp lệ";
    public static final String INVALID_PHONE = "Số điện thoại không hợp lệ (10-11 số)";
    public static final String PASSWORD_TOO_SHORT = "Mật khẩu phải có ít nhất 8 ký tự";
    public static final String PASSWORD_MISMATCH = "Mật khẩu không khớp";
    public static final String INVALID_FORMAT = "Định dạng không hợp lệ";
    public static final String VALUE_TOO_LONG = "Giá trị quá dài";
    public static final String VALUE_TOO_SHORT = "Giá trị quá ngắn";
    public static final String INVALID_VALUE = "Giá trị không hợp lệ";

    // Auth Messages
    public static final String LOGIN_SUCCESS = "Đăng nhập thành công";
    public static final String LOGOUT_SUCCESS = "Đăng xuất thành công";
    public static final String REGISTER_SUCCESS = "Đăng ký thành công";
    public static final String INVALID_CREDENTIALS = "Email hoặc mật khẩu không đúng";
    public static final String EMAIL_ALREADY_EXISTS = "Email đã được sử dụng";
    public static final String ACCOUNT_DISABLED = "Tài khoản đã bị vô hiệu hóa";
    public static final String TOKEN_EXPIRED = "Phiên đăng nhập đã hết hạn";
    public static final String INVALID_TOKEN = "Token không hợp lệ";
    public static final String PASSWORD_CHANGED = "Đổi mật khẩu thành công";

    // Product Messages
    public static final String PRODUCT_NOT_FOUND = "Không tìm thấy sản phẩm";
    public static final String PRODUCT_OUT_OF_STOCK = "Sản phẩm đã hết hàng";
    public static final String INSUFFICIENT_STOCK = "Không đủ số lượng trong kho";
    public static final String PRODUCT_INACTIVE = "Sản phẩm không còn kinh doanh";

    // Cart Messages
    public static final String CART_EMPTY = "Giỏ hàng trống";
    public static final String ADDED_TO_CART = "Đã thêm vào giỏ hàng";
    public static final String REMOVED_FROM_CART = "Đã xóa khỏi giỏ hàng";
    public static final String CART_UPDATED = "Cập nhật giỏ hàng thành công";
    public static final String INVALID_QUANTITY = "Số lượng không hợp lệ";

    // Order Messages
    public static final String ORDER_CREATED = "Đặt hàng thành công";
    public static final String ORDER_NOT_FOUND = "Không tìm thấy đơn hàng";
    public static final String ORDER_CANCELLED = "Hủy đơn hàng thành công";
    public static final String ORDER_CONFIRMED = "Xác nhận đơn hàng thành công";
    public static final String CANNOT_CANCEL_ORDER = "Không thể hủy đơn hàng này";
    public static final String ORDER_ALREADY_PROCESSED = "Đơn hàng đã được xử lý";

    // Payment Messages
    public static final String PAYMENT_SUCCESS = "Thanh toán thành công";
    public static final String PAYMENT_FAILED = "Thanh toán thất bại";
    public static final String PAYMENT_PENDING = "Thanh toán đang chờ xử lý";
    public static final String PAYMENT_CANCELLED = "Thanh toán đã bị hủy";

    // Voucher Messages
    public static final String VOUCHER_APPLIED = "Áp dụng mã giảm giá thành công";
    public static final String VOUCHER_INVALID = "Mã giảm giá không hợp lệ";
    public static final String VOUCHER_EXPIRED = "Mã giảm giá đã hết hạn";
    public static final String VOUCHER_NOT_STARTED = "Mã giảm giá chưa có hiệu lực";
    public static final String VOUCHER_ALREADY_USED = "Mã giảm giá đã được sử dụng";
    public static final String VOUCHER_NOT_APPLICABLE = "Mã giảm giá không áp dụng cho đơn hàng này";

    // File Upload Messages
    public static final String FILE_TOO_LARGE = "File quá lớn. Tối đa 5MB";
    public static final String INVALID_FILE_TYPE = "Loại file không được hỗ trợ";
    public static final String UPLOAD_SUCCESS = "Tải file thành công";
    public static final String UPLOAD_FAILED = "Tải file thất bại";

    private MessageConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
