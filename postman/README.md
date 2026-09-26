# Kiểm thử đăng tin bằng Postman

## Thiết lập

1. Import `ComeMyWay.postman_collection.json` và `Local.postman_environment.json`.
2. Chọn environment **ComeMyWay - Local (MySQL)**. Điền `username` và `password` của tài khoản phòng khám đang hoạt động; Spring Boot mặc định dùng `http://localhost:8080`, Node.js dùng `http://localhost:3002`.
3. Chạy toàn bộ collection, hoặc chạy **02 Login clinic** rồi **04 Create post** để thử nhanh. Đăng nhập tự lưu `accessToken`; đăng tin tự lưu `clinicId` và `createdPostId`. Chạy **07 Delete own post** để xóa bài vừa tạo.

Nếu đã import collection hoặc environment cũ, Postman không tự cập nhật theo file trên đĩa: import lại collection và kiểm tra `nodeBaseUrl` trong environment đang chọn. Tài liệu API trên trình duyệt: Spring Boot `http://localhost:8080/swagger-ui/index.html`, Node.js `http://localhost:3002/api-docs/`.

## Request đăng tin

`POST {{nodeBaseUrl}}/api/v1/clinic/posts` cần Bearer `{{accessToken}}` từ bước đăng nhập. Body gồm `title` và `content`; có [JSON mẫu](clinic-post.example.json) để gửi thủ công. Server tự lấy phòng khám từ token và lưu thời gian đăng.

`DELETE {{nodeBaseUrl}}/api/v1/clinic/posts/{{createdPostId}}` dùng cùng Bearer token, không cần body. Chỉ phòng khám đã đăng bài mới xóa được; bài không tồn tại hoặc thuộc phòng khám khác trả 404. Collection xác nhận bài đã biến mất khỏi feed sau khi xóa.

| Kết quả | Ý nghĩa |
| --- | --- |
| `201` | Bài đăng được tạo; `data.id` là ID của tin |
| `400` | Tiêu đề hoặc nội dung không hợp lệ |
| `401` | Thiếu hoặc sai access token |
| `503` ở `/health/ready` | Node.js chưa kết nối được MySQL |

Các folder trong collection kiểm tra health, đăng nhập/refresh, hồ sơ phòng khám, đăng tin, xóa tin, danh sách tin và validation. Mỗi lần chạy **04 Create post** sẽ tạo một tin mới; chạy toàn bộ collection sẽ xóa tin đó ở bước **07 Delete own post**.

## Chạy tự động

Từ thư mục gốc project:

```bash
npx --yes newman@6 run postman/ComeMyWay.postman_collection.json -e postman/Local.postman_environment.json
```

Điền thông tin đăng nhập trong environment trước khi chạy Newman; tránh commit file có mật khẩu.
