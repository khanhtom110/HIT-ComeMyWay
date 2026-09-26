# Backend Node.js — HIT-ComeMyWay

Dịch vụ Express dùng kiến trúc theo tầng của base HITProduct: router → validation → controller → service → model. Chức năng đăng tin phòng khám dùng chung MySQL và `JWT_SECRET` với backend Spring Boot. Tin chỉ cần `title` và `content`; thời điểm đăng được hệ thống tự lưu.

Lớp model của project này truy cập MySQL qua `mysql2`, phù hợp với dữ liệu tài khoản, phòng khám và token bị vô hiệu hóa đang có trong hệ thống. Express, Joi và các tầng xử lý tuân theo base được chọn.

## Cấu trúc thư mục

```text
backend-nodejs/
├── database/
│   └── migrations/                 # Các thay đổi schema theo thứ tự
├── src/
│   ├── configs/                    # env.config.js, db.config.js
│   ├── constants/                  # Giới hạn dữ liệu bài đăng
│   ├── models/                     # Truy vấn MySQL cho clinic và clinic-post
│   ├── validations/                # Schema Joi cho input
│   ├── middlewares/                # Xác thực, validation, 404, lỗi chung
│   ├── routers/                    # Khai báo URL và middleware; index.js ghép route
│   ├── controllers/                # Nhận input HTTP, gọi service, trả response
│   ├── services/                   # Nghiệp vụ, gọi model
│   ├── utils/                      # ApiError, catchAsync, response, JWT
│   ├── app.js                      # Tạo Express app và ghép các tầng
│   └── server.js                   # Nạp .env, kết nối MySQL, listen, đóng kết nối
├── test/                           # Kiểm thử HTTP và cấu hình với Node test runner
├── .env.example
├── .gitignore
├── package.json
└── package-lock.json
```

Mỗi tính năng có các file tương ứng trong `routers`, `validations`, `controllers`, `services`, `models`. Joi kiểm tra và chuẩn hóa input qua middleware; controller dùng `request.validated` và danh tính phòng khám đã xác thực. Service gọi model bằng dependency được truyền vào để kiểm thử mà không cần database thật. Khi thêm API, đăng ký router tại `src/routers/index.js` và ghép các dependency tại `src/app.js`/`src/server.js`.

## Cài đặt

Cần Node.js 20+, npm và MySQL đã có schema của backend Spring Boot.

1. Chạy `cd backend-nodejs` rồi `npm ci`.
2. Nếu chưa có `.env`, sao chép `.env.example` thành `.env`. Điền `JWT_SECRET` giống Spring Boot (ít nhất 32 ký tự), `DB_USERNAME`, `DB_PASSWORD` và các biến `DB_*` trỏ tới cùng database. `.env` được nạp tự động, biến môi trường của tiến trình được ưu tiên.
3. Chạy `database/migrations/001_create_clinic_posts.sql` trên database đó.
4. Chạy `npm run dev` khi phát triển hoặc `npm start` để khởi động. Server kiểm tra kết nối MySQL trước khi nghe tại `127.0.0.1:3001`; có thể đổi `HOST`/`PORT` trong `.env`.
5. Chạy `npm test` để kiểm tra API, phân quyền, validation và cấu hình bằng model giả. Các test không kết nối database thật.

`CORS_ORIGINS` là danh sách origin trình duyệt, phân cách bằng dấu phẩy, ví dụ `http://localhost:3000,https://app.example.com`. Request Android không gửi `Origin` vẫn được chấp nhận. JSON body giới hạn 64 KB; response dùng `{ statusCode, message, data, timestamp }` để tương thích Android.

## Docker và Postman

Xem [hướng dẫn Docker](../DOCKER.md) để build/chạy cả Node.js và Spring Boot bằng `compose.backends.yaml`. Hai dịch vụ dùng cổng riêng: Spring Boot `8080`, Node.js `3001`; có thể gọi trực tiếp bằng IP, không cần Nginx. Client cần base URL riêng cho API Node.js (`/api/v1/clinic/posts`, `/api/v1/public/clinic-posts`).

Import collection và environment trong [postman/](../postman/README.md) để chạy luồng đăng nhập Spring Boot → đăng tin Node.js và kiểm tra validation. Có environment local riêng cho phiên dữ liệu giả dev đang chạy trên máy.

## API

- `POST /api/v1/clinic/posts` với Bearer access token của phòng khám và JSON `{"title":"...","content":"..."}`. Trả 201 và bài đăng. Tiêu đề tối đa 200 ký tự, nội dung tối đa 10000 ký tự.
- `GET /api/v1/clinic/posts`: tối đa 50 tin mới nhất của phòng khám hiện tại.
- `GET /api/v1/public/clinic-posts`: tối đa 50 tin mới nhất công khai.
- `GET /health/live` (hoặc `/health`): trả 200 khi tiến trình xử lý HTTP được.
- `GET /health/ready`: trả 200 khi truy vấn kiểm tra MySQL thành công, 503 khi database không sẵn sàng.

JWT được kiểm tra chữ ký, hạn dùng, loại access token, vai trò và danh sách token bị vô hiệu hóa trong database. Các request lỗi dùng cùng định dạng response và không trả stack trace/chi tiết database cho client.
