# Kiểm thử API Node.js bằng Postman

## Thiết lập

1. Import `ComeMyWay.postman_collection.json` và `Local.postman_environment.json`.
2. Chọn environment **ComeMyWay - Local (MySQL)**. Điền `username`/`password` của tài khoản phòng khám đang hoạt động và `adminUsername`/`adminPassword` của tài khoản admin nếu chạy toàn bộ collection. Spring Boot mặc định dùng `http://localhost:8080`, Node.js dùng `http://localhost:3000` khi chạy trực tiếp hoặc bằng Docker.
3. Chạy toàn bộ collection, hoặc chạy riêng các folder cần thử. **02 Đăng nhập phòng khám** tự lưu `accessToken`; **04 Tạo bài đăng** tự lưu `clinicId` và `createdPostId`; **07 Xóa bài đăng của mình** xóa bài vừa tạo.

Nếu đã import collection hoặc environment cũ, Postman không tự cập nhật theo file trên đĩa: import lại environment và kiểm tra `nodeBaseUrl` trong environment đang chọn. Tài liệu API trên trình duyệt: Spring Boot `http://localhost:8080/swagger-ui/index.html`, Node.js `http://localhost:3000/api-docs/`.

## Request đăng tin

`POST {{nodeBaseUrl}}/api/v1/clinic/posts` cần Bearer `{{accessToken}}` từ bước đăng nhập. Body gồm `title`, `content` và tùy chọn `imageUrls` (mảng tối đa 10 URL HTTP(S)). Server tự lấy phòng khám từ token, lưu thời gian đăng và gán `status=PENDING`. Chạy migration 003 sau migration ảnh 002 trước khi sử dụng API kiểm duyệt. Bước **06 Ẩn bài chưa duyệt khỏi danh sách công khai** kiểm tra tin chưa duyệt không công khai.

Để kiểm tra duyệt tin, chạy **04 Tạo bài đăng**, đăng nhập admin qua **01 Đăng nhập quản trị viên**, rồi gọi `GET {{nodeBaseUrl}}/api/v1/admin/clinic-posts` và `PATCH {{nodeBaseUrl}}/api/v1/admin/clinic-posts/{{createdPostId}}/approve` với Bearer `{{adminAccessToken}}`. Sau khi duyệt, tin có `status=APPROVED`, `approvedBy`, `approvedAt`; feed công khai và `GET {{nodeBaseUrl}}/api/v1/public/clinic-posts/{{createdPostId}}` trả ảnh cùng nội dung. Có thể thao tác trực tiếp trong Swagger Node.js.

`DELETE {{nodeBaseUrl}}/api/v1/clinic/posts/{{createdPostId}}` dùng cùng Bearer token, không cần body. Chỉ phòng khám đã đăng bài mới xóa được; bài không tồn tại hoặc thuộc phòng khám khác trả 404. Collection xác nhận bài đã biến mất khỏi feed sau khi xóa.

| Kết quả | Ý nghĩa |
| --- | --- |
| `201` | Bài đăng được tạo; `data.id` là ID của tin |
| `400` | Tiêu đề hoặc nội dung không hợp lệ |
| `401` | Thiếu hoặc sai access token |
| `503` ở `/health/ready` | Node.js chưa kết nối được MySQL |

Các folder trong collection kiểm tra health, đăng nhập/refresh, hồ sơ phòng khám, đăng tin, xóa tin, danh sách tin và validation. Mỗi lần chạy **04 Tạo bài đăng** sẽ tạo một tin mới; chạy toàn bộ collection sẽ xóa tin đó ở bước **07 Xóa bài đăng của mình**.

## Duyệt bài admin

Request **00 Kiểm tra backend hỗ trợ duyệt bài** kiểm tra Swagger của server trước khi đăng nhập/tạo tin. Nếu request này thất bại, kiểm tra `nodeBaseUrl` và cập nhật backend trước khi chạy tiếp. Những request dùng `moderationPostId` yêu cầu bước tạo bài đã thành công. Lỗi 404 chỉ được chấp nhận khi `message` là **Không tìm thấy bài đăng**, không chấp nhận **Không tìm thấy API**.

Chạy toàn bộ folder **05 - Node.js - Duyệt bài phòng khám** theo thứ tự. Folder tự đăng nhập clinic và admin, tạo bài riêng có ảnh, kiểm tra tin chưa duyệt không công khai, chặn clinic tự duyệt, duyệt bằng ADMIN, kiểm tra duyệt lặp giữ nguyên `approvedBy`/`approvedAt`, kiểm tra danh sách và chi tiết công khai, rồi xóa bài thử nghiệm. Tổng cộng 20 request, gồm các trường hợp 400, 401, 404.

- Điền `username`, `password`, `adminUsername`, `adminPassword` trong environment trên máy local.
- Biến collection `postImageUrls` là chuỗi JSON chứa danh sách URL ảnh; mặc định dùng một ảnh mẫu Cloudinary. Có thể thay bằng URL từ API upload hiện có.
- `moderationPostId`, `moderationApprovedBy`, `moderationApprovedAt` tự lưu trong environment; không cần điền thủ công. Không dùng chung `createdPostId` của folder đăng tin cũ.
- Chạy trên môi trường kiểm thử: folder tạo, duyệt và xóa bài của chính lần chạy đó. Nếu dừng giữa chừng, gọi request **17 Phòng khám xóa bài thử nghiệm** để xóa bài đã tạo trước khi chạy lại.
- Import lại collection sau khi cập nhật file. Chạy migration `003_clinic_post_moderation.sql` một lần sau migration ảnh 002 trước khi dùng API kiểm duyệt.

## Thống kê admin

Folder **04 - Node.js - Thống kê quản trị** chạy độc lập với luồng phòng khám. Điền `adminUsername` và `adminPassword` của tài khoản vai trò `ADMIN` trong environment, rồi chạy lần lượt **01 Đăng nhập quản trị viên** và **02 Thống kê tài khoản**. Login lưu `adminAccessToken` riêng, không ghi đè `accessToken` phòng khám. **03 Thiếu token quản trị viên** kiểm tra API trả 401 khi thiếu token.

Nếu đã chạy seed trong `backend-nodejs`, dùng `adminUsername=cmw_test_admin` và lấy `adminPassword` từ file local `backend-nodejs/.env.seed.local`. File này chứa mật khẩu ngẫu nhiên và được Git ignore; không đưa mật khẩu vào collection được commit.

`GET {{nodeBaseUrl}}/api/v1/admin/statistics` trả `data.activeClinics` (phòng khám đã đổi mật khẩu mặc định, trạng thái `PENDING_PROFILE` hoặc `ACTIVE`), `data.inactiveClinics` (trạng thái ngoài `PENDING_PROFILE`/`ACTIVE`, gồm `PENDING_PASSWORD_CHANGE` và `NULL`) và `data.totalUsers` (chỉ vai trò `USER`). API yêu cầu access token `ADMIN` từ Spring Boot; token `CLINIC` không dùng được.

## Chạy tự động

### Khi gặp 404 “Không tìm thấy API”

Đây là endpoint chưa tồn tại trên server đang chạy (thường do container còn dùng image cũ), không phải trạng thái chờ duyệt. Không đổi test của danh sách chờ duyệt từ 200 sang 404 để làm test đạt.

1. Kiểm tra `nodeBaseUrl`: Node.js chạy trực tiếp hoặc bằng Docker local mặc định dùng `http://localhost:3000`.
2. Chạy migration `backend-nodejs/database/migrations/002_add_clinic_post_images.sql` rồi `backend-nodejs/database/migrations/003_clinic_post_moderation.sql` **mỗi file một lần** trên database dùng chung nếu chưa có các cột `image_urls`, `status`, `approved_by`, `approved_at`.
3. Cập nhật riêng Node.js, giữ nguyên cấu hình Compose local đang dùng:

```powershell
docker compose -p hit-comemyway --env-file .env.local-db -f compose.backends.yaml -f compose.local-db.yaml up -d --no-deps --build nodejs
```

4. Import lại collection để nhận tên request tiếng Việt và kiểm thử mới, rồi chạy folder **05 - Node.js - Duyệt bài phòng khám** từ bước 00.

### Chạy collection

Từ thư mục gốc project:

```bash
npx --yes newman@6 run postman/ComeMyWay.postman_collection.json -e postman/Local.postman_environment.json
```

Điền cả tài khoản phòng khám và admin trong environment trước khi chạy toàn bộ collection bằng Newman; tránh commit file có mật khẩu. Trong Postman UI có thể chạy riêng folder **04 - Node.js - Thống kê quản trị** khi chỉ muốn kiểm tra thống kê.
