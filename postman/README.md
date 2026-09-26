# Test API bằng Postman

## Test ngay cả Spring Boot và Node.js trên máy này

Môi trường Docker dev đã có MySQL, Redis và tài khoản phòng khám mẫu. Dùng **một environment** cho cả Spring Boot và Node.js:

1. `ComeMyWay.postman_collection.json`.
2. `Local.local.postman_environment.json` trên máy này, chọn **ComeMyWay - Local (MySQL)**.
3. Chạy toàn bộ collection theo thứ tự, hoặc chỉ folder **01 - Spring Boot** nếu muốn test riêng Spring Boot.

Environment local đã điền tài khoản dev, `springBaseUrl=http://localhost:8080` và `nodeBaseUrl=http://localhost:3002`. Body đăng nhập hiển thị `{{username}}` và `{{password}}`; chọn environment trên trước khi bấm Send. Đăng nhập tự lưu token; lấy hồ sơ hoặc đăng tin sẽ lưu `clinicId`. File environment có mật khẩu dev nên được Git ignore.

`Local.postman_environment.json` là bản mẫu không có mật khẩu để chia sẻ trong Git, cùng tên và URL với bản local. Nếu environment **Local (MySQL)** đã được import vào Postman từ trước, giá trị cũ trong Postman không tự đổi theo file: sửa `nodeBaseUrl` thành `http://localhost:3002`, lưu lại và đăng nhập lại trong environment đó. Cổng `3001` đang dùng cho API giả cũ trên máy này.

Để khởi động lại stack dev trên máy này, chạy lệnh sau trong PowerShell (chỉ sao chép dòng lệnh bên trong khung):

```powershell
& "$env:TEMP\comemyway-docker-dev\start.ps1"
```

Script dùng `compose.backends.yaml` cùng file override ở thư mục tạm để kết nối MySQL/Redis dev, giữ các cổng hiện tại. Dữ liệu nằm trong Docker volume `hit-comemyway_comemyway_dev_mysql`. MySQL và Redis chỉ mở cổng trong network Docker; không cần MySQL cài trên Windows. Cấu hình, mật khẩu và script seed nằm ngoài repo ở `%TEMP%\comemyway-docker-dev`; không có chế độ fake được thêm vào source ứng dụng.

Môi trường này phục vụ test đăng nhập, refresh, hồ sơ phòng khám và đăng tin. Email, Cloudinary, Gemini và Firebase chưa được cấu hình cho test dev này.

## Phiên API giả cũ (tùy chọn)

1. Import `ComeMyWay.postman_collection.json`.
2. Import `Dev-Fake.local.postman_environment.json` có sẵn trên máy này, chọn environment **ComeMyWay - Dev Fake (Node only)**.
3. Mở Collection Runner, chỉ chọn **02 - Node Clinic Posts** và **03 - Node Validation**, rồi Run.

API dev ở `http://127.0.0.1:3001`; environment chứa access token tạm của phòng khám mẫu ID `1`. File environment này bị Git ignore, không được commit. Token chỉ dùng với phiên API giả, hết hạn sau 24 giờ hoặc khi khởi động lại phiên; thời hạn nằm ở biến `tokenExpiresAt`. Folder Spring Boot cần backend và database thật nên không chạy bằng environment này.

Script và dữ liệu giả nằm ngoài repo tại `%TEMP%\comemyway-dev-db-10dff375`. Nếu phiên đã dừng, mở terminal và chạy:

```powershell
node "$env:TEMP\comemyway-dev-db-10dff375\server.mjs"
```

Sau khi khởi động lại, lấy `accessToken` mới từ `session.json` cùng thư mục, cập nhật giá trị local của biến `accessToken` trong Postman. Không đưa token này vào collection dùng chung.

## Test cả Spring Boot và Node.js với MySQL

1. Chạy hai backend theo [hướng dẫn Docker](../DOCKER.md); cấu hình cùng MySQL và `JWT_SECRET`, áp dụng migration `clinic_posts`.
2. Import `Local.postman_environment.json`, chọn **ComeMyWay - Local (MySQL)**.
3. Điền `username`, `password` của tài khoản phòng khám đã `ACTIVE` và có hồ sơ clinic; điều chỉnh `springBaseUrl`, `nodeBaseUrl` theo cổng đang chạy.
4. Run toàn bộ collection theo thứ tự mặc định. Không cần copy token thủ công.

| Folder | Nội dung |
| --- | --- |
| 01 - Spring Boot | Health → đăng nhập → refresh token → lấy hồ sơ phòng khám |
| 02 - Node Clinic Posts | Liveness/readiness → feed công khai → đăng tin → danh sách của phòng khám → xác nhận tin xuất hiện công khai |
| 03 - Node Validation | Tiêu đề trống/quá dài, thiếu/quá dài nội dung, thiếu/sai token, JSON lỗi, body quá 64 KB |

Request đăng nhập và refresh tự lưu `accessToken`, `refreshToken`; lấy hồ sơ lưu `clinicId`. Nếu gọi đăng tin ngay sau đăng nhập, request xác nhận `clinicId` hợp lệ và lưu nó vào cùng environment; khi đã lấy hồ sơ, request so sánh tác giả bài đăng với hồ sơ. Request đăng tin cũng lưu `createdPostId` cho các bước sau. Body tạo tin chỉ gửi `title` và `content`; server lấy phòng khám từ token.

Nếu muốn gửi thủ công, sao chép body từ [`clinic-post.example.json`](clinic-post.example.json) vào **Body → raw → JSON** của request `POST http://localhost:3002/api/v1/clinic/posts` (Docker dev). Chọn **Authorization → Bearer Token** và dùng `accessToken` lấy từ bước đăng nhập Spring Boot. JSON chỉ có `title` và `content`; server tự gắn phòng khám và thời gian đăng.

`postTitle` và `postContent` là biến collection, có thể sửa để thử dữ liệu khác. Mỗi lượt Runner tạo một bài đăng; API hiện chưa có endpoint xóa. Tài khoản USER không dùng được cho luồng đăng tin phòng khám.

### Test server bằng IP

Thay biến environment bằng địa chỉ thực tế sau khi deploy:

- `springBaseUrl`: `http://56.10.63.38` nếu Spring Boot được publish ở cổng 80; nếu dùng Compose mặc định thì `http://56.10.63.38:8080`.
- `nodeBaseUrl`: `http://56.10.63.38:3002` nếu Node đã deploy theo cổng mặc định và mở cổng 3002.

Các giá trị trên là địa chỉ cấu hình, không xác nhận dịch vụ Node đã được deploy lên server. Khi dùng Postman web để gọi localhost, chọn Desktop Agent; Postman Desktop gọi trực tiếp được.

## Chạy tự động bằng Newman

Cần Node.js/npm trên máy chạy lệnh. Từ thư mục gốc project:

```bash
# Hai backend Docker dev, environment đã điền tài khoản trên máy này
npx --yes newman@6 run postman/ComeMyWay.postman_collection.json -e postman/Local.local.postman_environment.json

# Phiên API giả: chỉ test Node
npx --yes newman@6 run postman/ComeMyWay.postman_collection.json -e postman/Dev-Fake.local.postman_environment.json --folder "02 - Node Clinic Posts" --folder "03 - Node Validation"

# Backend khác: dùng bản environment đã điền thông tin đăng nhập
npx --yes newman@6 run postman/ComeMyWay.postman_collection.json -e postman/My.local.postman_environment.json
```

Để test backend khác, sao chép `Local.postman_environment.json` thành `My.local.postman_environment.json` rồi điền biến. Các file `*.local.postman_environment.json` và thư mục báo cáo `newman/` đã được ignore để tránh commit token/mật khẩu.

## Khi test thất bại

- `ECONNREFUSED`: backend chưa chạy hoặc sai base URL/cổng.
- `401`: thiếu/sai/hết hạn token hoặc dùng token giữa phiên fake và backend thật. Đăng nhập lại hoặc cập nhật token dev.
- `403`: tài khoản không phải phòng khám đang hoạt động, thiếu hồ sơ clinic hoặc token bị thu hồi.
- Node `/health/ready` trả `503`: kết nối database chưa sẵn sàng.
- API đăng tin trả `500`: kiểm tra migration, quyền MySQL và log backend.
- Spring đăng nhập trả `429`: giới hạn tần suất đang áp dụng; đợi rồi chạy lại.
