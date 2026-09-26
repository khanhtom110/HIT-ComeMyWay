# Kiểm thử đăng tin bằng Postman

## Trên máy này

1. Import `ComeMyWay.postman_collection.json` và `Local.local.postman_environment.json`.
2. Chọn environment **ComeMyWay - Local (MySQL)**. Spring Boot dùng `http://localhost:8080`; Node.js dùng `http://localhost:3002`.
3. Chạy toàn bộ collection, hoặc chạy **02 Login clinic** rồi **04 Create post** để thử nhanh. Đăng nhập tự lưu `accessToken`; đăng tin tự lưu `clinicId` và `createdPostId`.

Nếu đã import collection hoặc environment cũ, Postman không tự cập nhật theo file trên đĩa: import lại collection và kiểm tra `nodeBaseUrl` trong environment đang chọn. File `Local.local.postman_environment.json` chứa tài khoản dev nên đã bị Git ignore. File `Local.postman_environment.json` là mẫu không có mật khẩu cho máy khác.

Stack Docker dev dùng MySQL và Redis local. Nếu cần khởi động lại trên máy này, chạy trong PowerShell:

```powershell
& "$env:TEMP\comemyway-docker-dev\start.ps1"
```

## Request đăng tin

`POST {{nodeBaseUrl}}/api/v1/clinic/posts` cần Bearer `{{accessToken}}` từ bước đăng nhập. Body gồm `title` và `content`; có [JSON mẫu](clinic-post.example.json) để gửi thủ công. Server tự lấy phòng khám từ token và lưu thời gian đăng.

| Kết quả | Ý nghĩa |
| --- | --- |
| `201` | Bài đăng được tạo; `data.id` là ID của tin |
| `400` | Tiêu đề hoặc nội dung không hợp lệ |
| `401` | Thiếu hoặc sai access token |
| `503` ở `/health/ready` | Node.js chưa kết nối được MySQL |

Các folder trong collection kiểm tra health, đăng nhập/refresh, hồ sơ phòng khám, đăng tin, danh sách tin và validation. Mỗi lần chạy **04 Create post** sẽ tạo một tin mới.

## Chạy tự động

Từ thư mục gốc project:

```bash
npx --yes newman@6 run postman/ComeMyWay.postman_collection.json -e postman/Local.local.postman_environment.json
```
