# Clinic posts (Node.js)

Dịch vụ đăng tin phòng khám dùng chung MySQL và `JWT_SECRET` với backend Spring Boot. Tin chỉ cần `title` và `content`; thời điểm đăng được hệ thống tự lưu, không phải trường nhập.

## Cài đặt

1. Chạy `sql/001_clinic_posts.sql` trên cùng database MySQL của Spring Boot.
2. Đặt `JWT_SECRET` giống backend, cùng `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD` nếu khác mặc định trong `src/server.js`.
3. Chạy `npm ci` rồi `npm start` (Node.js 20+). Dịch vụ mặc định nghe cổng `3001`.
4. Định tuyến `/api/v1/clinic/posts` và `/api/v1/public/clinic-posts` từ cùng host API mà Android đang dùng sang dịch vụ này. Chỉ chuyển hai đường dẫn đó; các API khác tiếp tục đến Spring Boot. Nên đặt dịch vụ sau reverse proxy, không mở cổng 3001 ra Internet.

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
