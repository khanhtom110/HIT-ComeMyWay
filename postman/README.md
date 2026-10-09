# Kiểm thử API Node.js bằng Postman

## Thiết lập

1. Import `ComeMyWay.postman_collection.json` và `Local.postman_environment.json`.
2. Chọn environment **ComeMyWay - Local (MySQL)**. Điền `username`/`password` của tài khoản phòng khám đang hoạt động và `adminUsername`/`adminPassword` của tài khoản admin nếu chạy toàn bộ collection. Spring Boot mặc định dùng `http://localhost:8080`, Node.js dùng `http://localhost:3000` khi chạy trực tiếp hoặc bằng Docker.
3. Chạy toàn bộ collection, hoặc chạy riêng các folder cần thử. **02 Đăng nhập phòng khám** tự lưu `accessToken`; **04 Tạo bài đăng** tự lưu `clinicId` và `createdPostId`; **07 Xóa bài đăng của mình** xóa bài vừa tạo.

Nếu đã import collection hoặc environment cũ, Postman không tự cập nhật theo file trên đĩa: import lại environment và kiểm tra `nodeBaseUrl` trong environment đang chọn. Tài liệu API trên trình duyệt: Spring Boot `http://localhost:8080/swagger-ui/index.html`, Node.js `http://localhost:3000/api-docs/`.

## Request đăng tin

`POST {{nodeBaseUrl}}/api/v1/clinic/posts` cần Bearer `{{accessToken}}` từ bước đăng nhập. Body gồm `title`, `content` và tùy chọn `imageUrls` (mảng tối đa 10 URL HTTP(S)). Server tự lấy phòng khám từ token, lưu thời gian đăng và gán `status=PENDING`, `approvedBy=null`, `approvedAt=null`; bài chưa công khai. Chạy migration 003 sau migration ảnh 002, rồi migration 004 để thêm trạng thái từ chối. Bước **06 Bài chưa duyệt không công khai** kiểm tra hành vi này.

Response có `clinicThumbnailUrl` lấy từ ảnh đại diện hiện tại của phòng khám; nếu hồ sơ chưa có ảnh thì giá trị là `null`. `imageUrls` là ảnh nội dung bài đăng do client gửi, không phải ảnh đại diện phòng khám.

Sau **04 Tạo bài đăng**, phòng khám xem bài trong danh sách của mình. Admin gọi `GET {{nodeBaseUrl}}/api/v1/admin/clinic-posts?status=PENDING`, rồi `PATCH {{nodeBaseUrl}}/api/v1/admin/clinic-posts/{{id}}/approve` hoặc `/reject` với Bearer `{{adminAccessToken}}`. Có 3 trạng thái: `PENDING` (chưa duyệt), `APPROVED` (đã duyệt), `REJECTED` (từ chối duyệt). Chỉ bài `APPROVED` được xem công khai. Danh sách admin hỗ trợ `limit` (1–50) và `beforeId`: nếu response có `pagination.hasMore=true`, truyền `pagination.nextBeforeId` vào `beforeId` ở request tiếp theo, giữ nguyên bộ lọc `status`.

`DELETE {{nodeBaseUrl}}/api/v1/clinic/posts/{{createdPostId}}` dùng cùng Bearer token, không cần body. Chỉ phòng khám đã đăng bài mới xóa được; bài không tồn tại hoặc thuộc phòng khám khác trả 404. Collection xác nhận bài đã biến mất khỏi feed sau khi xóa.

| Kết quả | Ý nghĩa |
| --- | --- |
| `201` | Bài đăng được tạo; `data.id` là ID của tin |
| `400` | Tiêu đề hoặc nội dung không hợp lệ |
| `401` | Thiếu hoặc sai access token |
| `503` ở `/health/ready` | Node.js chưa kết nối được MySQL |

Các folder trong collection kiểm tra health, đăng nhập/refresh, hồ sơ phòng khám, đăng tin, xóa tin, danh sách tin và validation. Mỗi lần chạy **04 Tạo bài đăng** sẽ tạo một tin mới; chạy toàn bộ collection sẽ xóa tin đó ở bước **07 Xóa bài đăng của mình**.

## Duyệt bài và API admin

Request **00 Kiểm tra backend hỗ trợ duyệt bài** kiểm tra Swagger của server trước khi đăng nhập/tạo tin. Nếu request này thất bại, kiểm tra `nodeBaseUrl` và cập nhật backend trước khi chạy tiếp. Những request dùng `moderationPostId` yêu cầu bước tạo bài đã thành công. Lỗi 404 chỉ được chấp nhận khi `message` là **Không tìm thấy bài đăng**, không chấp nhận **Không tìm thấy API**.

Chạy toàn bộ folder **05 - Node.js - Duyệt bài phòng khám** theo thứ tự. Folder tự đăng nhập clinic/admin, tạo bài có ảnh, kiểm tra bài chờ duyệt chưa công khai, chặn clinic gọi API admin, duyệt và kiểm tra công khai. Sau đó tạo bài riêng để kiểm tra từ chối, danh sách `REJECTED`, phân quyền, ID không hợp lệ và việc không công khai bài bị từ chối. Có bước xem chi tiết admin ở cả 3 trạng thái, kiểm tra số huy hiệu qua `/counts`, phân trang với `limit`/`beforeId`, và kiểm tra phân quyền. Các bài thử nghiệm được xóa cuối luồng.

- Điền `username`, `password`, `adminUsername`, `adminPassword` trong environment trên máy local.
- Biến collection `postImageUrls` chứa chuỗi JSON danh sách URL ảnh.
- `moderationPostId`, `moderationApprovedAt`, `rejectedPostId` tự lưu; không cần điền thủ công.
- Gọi duyệt hoặc từ chối lặp trả 200; đổi quyết định đã xử lý trả 409.
- Import lại collection sau khi cập nhật. Chạy migration 004 sau 003 một lần; migration giữ nguyên trạng thái bài đã có.
- Nếu dừng giữa luồng, dùng API xóa bài của phòng khám để xóa các ID bài thử nghiệm trước khi chạy lại.

## Thống kê admin

Folder **04 - Node.js - Thống kê quản trị** chạy độc lập với luồng phòng khám. Điền `adminUsername` và `adminPassword` của tài khoản vai trò `ADMIN` trong environment, rồi chạy lần lượt **01 Đăng nhập quản trị viên** và **02 Thống kê tài khoản**. Login lưu `adminAccessToken` riêng, không ghi đè `accessToken` phòng khám. **03 Thiếu token quản trị viên** kiểm tra API trả 401 khi thiếu token.

Nếu đã chạy seed trong `backend-nodejs`, dùng `adminUsername=cmw_test_admin` và lấy `adminPassword` từ file local `backend-nodejs/.env.seed.local`. File này chứa mật khẩu ngẫu nhiên và được Git ignore; không đưa mật khẩu vào collection được commit.

`GET {{nodeBaseUrl}}/api/v1/admin/statistics` trả `data.activeClinics` (phòng khám đã đổi mật khẩu mặc định, trạng thái `PENDING_PROFILE` hoặc `ACTIVE`), `data.inactiveClinics` (trạng thái ngoài `PENDING_PROFILE`/`ACTIVE`, gồm `PENDING_PASSWORD_CHANGE` và `NULL`) và `data.totalUsers` (chỉ vai trò `USER`). API yêu cầu access token `ADMIN` từ Spring Boot; token `CLINIC` không dùng được.

## Chạy tự động

### Khi gặp 404 “Không tìm thấy API”

Đây là endpoint chưa tồn tại trên server đang chạy (thường do container còn dùng image cũ), không phải trạng thái chờ duyệt. Không đổi test của danh sách chờ duyệt từ 200 sang 404 để làm test đạt.

1. Kiểm tra `nodeBaseUrl`: Node.js chạy trực tiếp hoặc bằng Docker local mặc định dùng `http://localhost:3000`.
2. Chạy migration `backend-nodejs/database/migrations/002_add_clinic_post_images.sql` rồi `backend-nodejs/database/migrations/003_clinic_post_moderation.sql` rồi `backend-nodejs/database/migrations/004_clinic_post_rejection.sql` **mỗi file một lần** trên database dùng chung. Bỏ qua migration 002/003 nếu đã có các cột tương ứng; chạy 004 nếu enum `status` chưa có `REJECTED`.
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
