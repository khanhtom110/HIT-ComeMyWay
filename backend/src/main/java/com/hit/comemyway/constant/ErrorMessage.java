package com.hit.comemyway.constant;

public final class ErrorMessage {
    private ErrorMessage() {} // Chặn khởi tạo class cha

    // Lỗi hệ thống chung
    public static final String EXCEPTION_GENERAL = "Đã có lỗi hệ thống xảy ra, vui lòng thử lại sau.";
    public static final String UNAUTHORIZED = "Không có quyền truy cập, vui lòng đăng nhập.";
    public static final String FORBIDDEN = "Bạn không có quyền thực hiện thao tác này.";
    public static final String BAD_REQUEST = "Dữ liệu yêu cầu không hợp lệ.";
    public static final String FORBIDDEN_UPDATE_DELETE = "Bạn không có quyền cập nhật hoặc xóa dữ liệu này.";
    public static final String UPLOAD_IMAGE_FAIL = "Tải ảnh lên thất bại. Vui lòng kiểm tra lại.";

    // Lỗi validation DTO chung
    public static final String INVALID_SOME_THING_FIELD = "Dữ liệu trường này không hợp lệ.";
    public static final String INVALID_FORMAT_SOME_THING_FIELD = "Định dạng dữ liệu không chính xác.";
    public static final String INVALID_SOME_THING_FIELD_IS_REQUIRED = "Trường dữ liệu này là bắt buộc.";
    public static final String NOT_BLANK_FIELD = "Trường dữ liệu này không được để trống.";
    public static final String INVALID_FORMAT_PASSWORD = "Mật khẩu không đúng định dạng (tối thiểu 8 ký tự, bao gồm chữ và số).";

    // Nhóm lỗi Xác thực (Auth)
    public static final class Auth {
        private Auth() {} // Chặn khởi tạo class con

        public static final String INVALID_CREDENTIALS = "Tên đăng nhập hoặc mật khẩu không chính xác.";
        public static final String INVALID_REFRESH_TOKEN = "Mã làm mới (Refresh Token) không hợp lệ.";
        public static final String EXPIRED_REFRESH_TOKEN = "Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại.";
        public static final String LOGIN_FAIL = "Đăng nhập thất bại.";
        public static final String GET_TOKEN_CLAIM_SET_FAIL = "Lỗi khi trích xuất thông tin mã xác thực.";
        public static final String TOKEN_INVALIDATED = "Mã xác thực đã bị vô hiệu hóa.";
        public static final String MALFORMED_TOKEN = "Định dạng mã xác thực không hợp lệ.";
        public static final String TOKEN_ALREADY_INVALIDATED = "Mã xác thực này đã được đăng xuất hoặc bị hủy bỏ trước đó.";
    }

    // Nhóm lỗi Người dùng (User)
    public static final class User {
        private User() {}

        public static final String USER_NOT_EXISTED = "Người dùng không tồn tại trong hệ thống.";
        public static final String USERNAME_EXISTED = "Tên đăng nhập này đã được sử dụng. Vui lòng chọn tên khác.";
        public static final String EMAIL_EXISTED = "Địa chỉ email này đã được đăng ký.";
    }

    // Nhóm lỗi Quản trị viên (Admin)
    public static final class Admin {
        private Admin() {}

        public static final String NOT_ADMIN = "Yêu cầu quyền Quản trị viên để thực hiện hành động này.";
    }
}