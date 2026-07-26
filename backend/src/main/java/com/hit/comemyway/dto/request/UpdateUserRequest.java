package com.hit.comemyway.dto.request;

import com.hit.comemyway.constant.CommonConstant;
import com.hit.comemyway.constant.ErrorMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
// @formatter:off
        @Schema(description = "Họ và tên", example = "Nguyễn Công Phượng")
        @Size(min = 4, max = CommonConstant.FULLNAME_LENGTH, message = ErrorMessage.INVALID_FORMAT_FULLNAME)
        String fullName,

        @Schema(description = "Số điện thoại", example = "0383553886")
        @Pattern(regexp = CommonConstant.PHONE_REGEX, message = ErrorMessage.INVALID_FORMAT_PHONE)
        String phone,

        @Schema(description = "Địa chỉ nhà", example = "Số 1, đường Nhổn, Hà Nội")
        @Size(max = CommonConstant.ADDRESS_LENGTH, message = ErrorMessage.INVALID_FORMAT_ADDRESS)
        String homeAddress,

        @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
        @Email(message = ErrorMessage.INVALID_FORMAT_EMAIL)
        @Schema(description = "Địa chỉ email cá nhân", example = "khanhn.nv@gmail.com")
        String email,

        @Schema(description = "Ảnh đại diện", example = "https://example.com/user.jpg")
        String avatar,

        @Schema(description = "Sở thích")
        String hobby
) {
}
