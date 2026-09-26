# Backend Node.js — HIT-ComeMyWay

Dịch vụ đăng tin phòng khám dùng chung MySQL và `JWT_SECRET` với backend Spring Boot. Tin chỉ cần `title` và `content`; thời điểm đăng được hệ thống tự lưu, không phải trường nhập.

## Cấu trúc thư mục

```text
backend-nodejs/
├── database/
│   └── migrations/                 # Các thay đổi schema theo thứ tự
├── src/
│   ├── config/                     # Biến môi trường và kết nối MySQL
│   ├── http/                       # Đọc JSON, định dạng response, lỗi HTTP
│   ├── middlewares/                # Xác thực phòng khám và xử lý lỗi chung
│   ├── modules/
│   │   └── clinic-posts/
│   │       ├── clinic-post.routes.js
│   │       ├── clinic-post.controller.js
│   │       ├── clinic-post.service.js
│   │       ├── clinic-post.repository.js
│   │       └── clinic-post.validation.js
│   ├── security/                   # Kiểm tra chữ ký và claims JWT
│   ├── app.js                      # Ghép các module, tạo HTTP server để kiểm thử
│   └── server.js                   # Khởi tạo cấu hình, database và lắng nghe cổng
├── tests/
│   ├── integration/                # Kiểm thử HTTP với repository giả
│   └── unit/                       # Kiểm thử xử lý dữ liệu dùng chung
├── .env.example
├── .gitignore
├── package.json
└── package-lock.json
```

Mỗi tính năng đặt trong một thư mục thuộc `src/modules/`. Route ánh xạ URL và gắn xác thực; controller xử lý HTTP; service thực hiện nghiệp vụ và validation; repository truy vấn database. Các dependency được truyền vào qua hàm khởi tạo để kiểm thử mà không cần database thật. Khi thêm tính năng, tạo module cùng cấu trúc rồi đăng ký route tại `src/app.js`.

## Cài đặt

1. Từ thư mục gốc project, chạy `cd backend-nodejs`.
2. Chạy `database/migrations/001_create_clinic_posts.sql` trên cùng database MySQL của Spring Boot.
3. Cấu hình các biến môi trường theo `.env.example`: `JWT_SECRET` phải giống Spring Boot; thông tin `DB_*` trỏ tới cùng database. Giá trị mặc định nằm trong `src/config/env.js`.
4. Chạy `npm ci` rồi `npm start` (Node.js 20+). Dịch vụ mặc định nghe tại `127.0.0.1:3001`. Nếu dùng file `.env`, sao chép `.env.example`, điền giá trị rồi chạy `node --env-file=.env src/server.js` với Node.js 20.6+; `npm start` đọc biến môi trường của tiến trình.
5. Định tuyến `/api/v1/clinic/posts` và `/api/v1/public/clinic-posts` từ cùng host API mà Android đang dùng sang dịch vụ này. Chỉ chuyển hai đường dẫn đó; các API khác tiếp tục đến Spring Boot. Nên đặt dịch vụ sau reverse proxy, không mở cổng 3001 ra Internet.

Ví dụ Nginx:

```nginx
location = /api/v1/clinic/posts {
    proxy_pass http://127.0.0.1:3001;
    proxy_set_header Authorization $http_authorization;
}
location = /api/v1/public/clinic-posts {
    proxy_pass http://127.0.0.1:3001;
}
```

## API

- `POST /api/v1/clinic/posts` với Bearer access token của phòng khám và JSON `{"title":"...","content":"..."}`. Trả 201 và bài đăng. Tiêu đề tối đa 200 ký tự, nội dung tối đa 10000 ký tự.
- `GET /api/v1/clinic/posts`: tối đa 50 tin mới nhất của phòng khám hiện tại.
- `GET /api/v1/public/clinic-posts`: tối đa 50 tin mới nhất công khai.
- `GET /health`: kiểm tra tiến trình.

JWT được kiểm tra chữ ký, hạn dùng, loại access token, vai trò và danh sách token bị vô hiệu hóa trong database. Chạy `npm test` để kiểm tra API bằng repository giả, không cần MySQL.
