# Chạy hai backend bằng Docker

Chạy lệnh từ thư mục gốc project. Cần Docker Engine/Desktop và Docker Compose **2.30+** (để đọc `env_file` với `format: raw`). Không cần cài Node.js, Maven hoặc Java trên máy host để build.

## Stack dev đã chuẩn bị trên máy này

Để test cả hai backend ngay, dùng một environment `postman/Local.local.postman_environment.json` theo [hướng dẫn Postman](postman/README.md). Spring Boot đang dùng cổng `8080`, Node.js dùng cổng `3002`; MySQL và Redis dev chạy trong cùng network Docker. MySQL lưu dữ liệu trong volume `hit-comemyway_comemyway_dev_mysql`.

Khởi động lại bằng PowerShell:

```powershell
& "$env:TEMP\comemyway-docker-dev\start.ps1"
```

Script dùng file Compose bên dưới cùng override local `%TEMP%\comemyway-docker-dev\compose.override.json`. Override chứa cấu hình và thông tin đăng nhập dev; không được commit. Các phần tiếp theo mô tả cách build và chạy file Compose cơ sở với database do bạn cấu hình.

## 1. Build image

```bash
docker compose -f compose.backends.yaml build
```

Compose đọc hai file local `backend/.env` và `backend-nodejs/.env`; tạo chúng trước khi dùng Compose. Có thể build từng image độc lập với `.env`:

```bash
docker build -t comemyway-nodejs:dev ./backend-nodejs
docker build -t comemyway-springboot:dev ./backend
```

- Node.js: Node 24, `npm ci` theo lockfile, chỉ copy source và dependency runtime, chạy dưới user `node`.
- Spring Boot: Maven/Java 21 biên dịch trong stage build; stage runtime chỉ chứa JRE 21 và JAR, chạy dưới user `app`. Lệnh build bỏ qua chạy test cần hạ tầng (`-DskipTests`) và formatter; vẫn biên dịch source và test source.
- `.dockerignore` giới hạn build context; `.env`, token Postman và Firebase service-account JSON không được copy vào image.

## 2. Database và cấu hình runtime

File Compose này có **hai service backend**, không khởi tạo MySQL. Mặc định hai service kết nối MySQL trên **máy host Docker**, cổng `3306`, database `pet_heartbeat_db` qua `host.docker.internal`. `localhost` bên trong container là chính container đó.

Trước khi chạy:

1. MySQL phải truy cập được từ container, database `pet_heartbeat_db` đã tồn tại và tài khoản có quyền kết nối. Nếu dùng Docker Engine trên Linux, MySQL cần lắng nghe trên địa chỉ mà container truy cập được.
2. `backend/.env` chứa các biến Spring Boot đang dùng: `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `JWT_ACCESS_EXPIRATION`, `JWT_REFRESH_EXPIRATION`, `REDIS_HOST`, `REDIS_PORT`, `REDIS_PASSWORD`, `BREVO_API_KEY`, `BREVO_SENDER_EMAIL`, `CLOUDINARY_CLOUD_NAME`, `CLOUDINARY_API_KEY`, `CLOUDINARY_API_SECRET`, `GEMINI_API_KEY`.
3. `backend-nodejs/.env` chứa `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET` giống Spring Boot. Có thể thêm `CORS_ORIGINS` nếu gọi từ trình duyệt. Compose đặt lại `HOST=0.0.0.0`, `PORT=3001` và các biến địa chỉ database.
4. Spring Boot cần Redis theo cấu hình hiện tại (SSL đang bật); đăng nhập/refresh cần Redis hoạt động.
5. Khởi động Spring Boot để tạo/cập nhật schema, rồi chạy SQL trong `backend-nodejs/database/migrations/001_create_clinic_posts.sql` trên cùng database trước khi dùng API đăng tin. Tài khoản phòng khám test cần role `CLINIC`, trạng thái `ACTIVE` và có hồ sơ clinic.

Compose ưu tiên `DOCKER_DB_URL` cho Spring Boot và `DOCKER_DB_HOST`, `DOCKER_DB_PORT`, `DOCKER_DB_NAME` cho Node.js. Đặt các biến này khi dùng MySQL ở máy khác. Ví dụ PowerShell (thay `mysql-host` bằng hostname/IP thực):

```powershell
$env:DOCKER_DB_URL = 'jdbc:mysql://mysql-host:3306/pet_heartbeat_db?serverTimezone=UTC'
$env:DOCKER_DB_HOST = 'mysql-host'
$env:DOCKER_DB_PORT = '3306'
$env:DOCKER_DB_NAME = 'pet_heartbeat_db'
```

Hai backend phải trỏ tới cùng database và cùng `JWT_SECRET` để dùng chung access token. Kết nối TLS MySQL, nếu nhà cung cấp yêu cầu, phải cấu hình phù hợp ở cả hai dịch vụ; Node hiện dùng cấu hình pool trong `src/configs/db.config.js`.

Firebase push notification cần file service account riêng. Khi sử dụng chức năng đó, mount file read-only vào `/etc/secrets/firebase-service-account.json`; không đưa file vào image.

## 3. Chạy và test

```bash
docker compose -f compose.backends.yaml up -d springboot
# Sau khi schema Spring Boot sẵn sàng, chạy migration SQL nêu trên.
docker compose -f compose.backends.yaml up -d nodejs
docker compose -f compose.backends.yaml ps
docker compose -f compose.backends.yaml logs --tail=100 springboot nodejs
```

| Dịch vụ | URL local | Health endpoint |
| --- | --- | --- |
| Spring Boot | `http://localhost:8080` | `/api/v1/public/health` |
| Node.js | `http://localhost:3002` | `/health/live`, `/health/ready` |

Node `/health/ready` kiểm tra kết nối MySQL. Docker healthcheck dùng `/health/live` để kiểm tra tiến trình HTTP. Health trả 200 không thay thế việc test đăng nhập và đăng tin trong [Postman](postman/README.md).

Chạy bằng IP server với hai cổng riêng, không cần Nginx hay tên miền. Ví dụ Spring Boot `http://56.10.63.38:8080`, Node.js `http://56.10.63.38:3002` **sau khi deploy** và mở các cổng tương ứng. Client phải gọi đúng base URL của từng dịch vụ. Nếu muốn Spring Boot phục vụ tại `http://56.10.63.38/`, đặt `SPRING_HTTP_PORT=80` trước khi chạy Compose (cổng 80 phải còn trống).

Đổi cổng host bằng `SPRING_HTTP_PORT` và `NODE_HTTP_PORT`; cập nhật `springBaseUrl`, `nodeBaseUrl` trong Postman tương ứng. Cổng `3001` là cổng bên trong container Node.js, cổng host mặc định là `3002`.

```bash
docker compose -f compose.backends.yaml down
```

Lệnh trên dừng/xóa container và network của stack. Database ở ngoài stack.

## Dữ liệu giả dev hiện tại

Phiên API giả cũ đang chạy riêng ở `http://127.0.0.1:3001`, với script/data trong thư mục tạm ngoài repository. Nếu cần kiểm tra riêng phiên này, dùng `postman/Dev-Fake.local.postman_environment.json` theo [hướng dẫn Postman](postman/README.md). Image Docker chạy source bình thường và cần MySQL khi khởi động; không có chế độ fake DB được thêm vào source.
