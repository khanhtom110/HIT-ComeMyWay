package com.hit.comemyway.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(@Schema(description="Mã thông báo truy cập (JWT) dùng để xác thực các API yêu cầu quyền",example="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJraGFuaG5ndXllbnZpZXQiLCJpc1JlZnJlc2giOmZhbHNlLCJleHAiOjE3ODI1NTg2NzUsImlhdCI6MTc4MjU1Njg3NSwidXNlcklkIjoxMiwianRpIjoiNTZhNjlhYTUtNDZkOC00MjE3LTg2YjUtZjFkMTY4NGYzY2ZjIiwiYXV0aG9yaXRpZXMiOiJVU0VSIn0.eJs6dxnKx-nil1gbiB9rfoa1V3CsGmMLkjAscfKtaBM")String accessToken,

@Schema(description="Mã dùng để duy trì và gia hạn phiên đăng nhập mà không cần người dùng nhập lại thông tin",example="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJraGFuaG5ndXllbnZpZXQiLCJpc1JlZnJlc2giOnRydWUsImV4cCI6MTc4MzE2MTY3NSwiaWF0IjoxNzgyNTU2ODc1LCJqdGkiOiI2NTdlN2JkMy02YzgzLTQ4ODUtYThiZC1lYzA1Njk3NjU5MDgifQ.f052ug0mYE3X7-rKLjLfCczWWlTEtnOCSc4JnWp9gu4")String refreshToken){}
