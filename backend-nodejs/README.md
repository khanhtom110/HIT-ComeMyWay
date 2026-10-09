# Backend Node.js — HIT-ComeMyWay

Dịch vụ Express dùng kiến trúc theo tầng của base HITProduct: router → validation → controller → service → model. Chức năng đăng tin phòng khám và thống kê admin dùng chung MySQL và `JWT_SECRET` với backend Spring Boot. Tin chỉ cần `title` và `content`; thời điểm đăng được hệ thống tự lưu.

Lớp model của project này truy cập MySQL qua `mysql2`, phù hợp với dữ liệu tài khoản, phòng khám và token bị vô hiệu hóa đang có trong hệ thống. Express, Joi và các tầng xử lý tuân theo base được chọn.

## Cấu trúc thư mục

```text
backend-nodejs/
├── database/
│   └── migrations/                 # Các thay đổi schema theo thứ tự
├── src/
│   ├── configs/                    # env.config.js, db.config.js
│   ├── constants/                  # client/ và index.js
│   ├── docs/                       # Tài liệu OpenAPI cho Swagger UI
│   ├── models/                     # admin/, client/ và index.js; truy vấn MySQL
│   ├── validations/                # client/ và index.js; schema Joi
│   ├── middlewares/                # admin/, client/, index.js và middleware dùng chung
│   ├── routers/                    # admin/, client/, index.js và health route
│   ├── controllers/                # admin/, client/, index.js và health controller
│   ├── services/                   # client/ và index.js; nghiệp vụ đăng tin
│   ├── utils/                      # ApiError, catchAsync, response, JWT
│   ├── app.js                      # Tạo Express app và ghép các tầng
│   └── server.js                   # Nạp .env, kết nối MySQL, listen, đóng kết nối
├── test/                           # admin/, client/ và kiểm thử cấu hình
├── .gitignore
├── package.json
└── package-lock.json
```

Mỗi tầng đặt file riêng trong `admin/` hoặc `client/` theo vai trò; `index.js` ở ngoài hai thư mục này xuất các thành phần của tầng. Middleware, controller và route health dùng chung nằm trực tiếp trong tầng tương ứng. Joi kiểm tra input bài đăng; controller dùng `request.validated` và danh tính phòng khám đã xác thực. API thống kê admin đọc trực tiếp từ model vì không có bước nghiệp vụ riêng. Khi thêm API, đăng ký router tại `src/routers/index.js`, ghép dependency tại `src/app.js`/`src/server.js` và cập nhật OpenAPI tại `src/docs/openapi.js`.

## Cài đặt

Cần Node.js 20+, npm và MySQL đã có schema của backend Spring Boot.

1. Chạy `cd backend-nodejs` rồi `npm ci`.
2. Sao chép `.env.example` thành `.env`, rồi điền `JWT_SECRET` giống Spring Boot (ít nhất 32 ký tự), `DB_USERNAME`, `DB_PASSWORD` và các biến `DB_*` trỏ tới cùng database. `.env` được nạp tự động, biến môi trường của tiến trình được ưu tiên.
3. Chạy lần lượt `database/migrations/001_create_clinic_posts.sql`, `database/migrations/002_add_clinic_post_images.sql`, `database/migrations/003_clinic_post_moderation.sql` và `database/migrations/004_clinic_post_rejection.sql` trên database đó. Mỗi migration chỉ chạy một lần. Migration 003 đưa tin cũ về `PENDING`; migration 004 thêm `REJECTED` và giữ nguyên trạng thái bài hiện có.
4. Chạy `npm run dev` khi phát triển hoặc `npm start` để khởi động. Server kiểm tra kết nối MySQL trước khi nghe tại `127.0.0.1:3000`; có thể đổi `HOST`/`PORT` trong `.env`.
5. Chạy `npm test` để kiểm tra API, phân quyền, validation và cấu hình.

`CORS_ORIGINS` là danh sách origin trình duyệt, phân cách bằng dấu phẩy, ví dụ `http://localhost:3000,https://app.example.com`. Request Android không gửi `Origin` vẫn được chấp nhận. JSON body giới hạn 64 KB; response dùng `{ statusCode, message, data, timestamp }` để tương thích Android.

## Docker và Postman

Xem [hướng dẫn Docker](../DOCKER.md) để build/chạy cả Node.js và Spring Boot bằng `compose.backends.yaml`. Hai dịch vụ dùng cổng host riêng: Spring Boot `8080`, Node.js `3000`; có thể gọi trực tiếp bằng IP, không cần Nginx. Client cần base URL riêng cho API Node.js (`/api/v1/clinic/posts`, `/api/v1/public/clinic-posts`, `/api/v1/admin/statistics`).

Import collection và environment trong [postman/](../postman/README.md) để chạy riêng luồng đăng tin phòng khám hoặc thống kê admin.

## API

Swagger UI: `http://localhost:3000/api-docs/` khi chạy bằng Docker hoặc Node.js trực tiếp. Tài liệu OpenAPI dạng JSON ở `/api-docs/openapi.json`. Chọn **Authorize**: dùng `clinicBearer` cho tài khoản phòng khám và `adminBearer` cho tài khoản admin; cả hai access token lấy từ API đăng nhập Spring Boot. Swagger của Spring Boot ở `http://localhost:8080/swagger-ui/index.html`.

- `POST /api/v1/clinic/posts` với Bearer access token của phòng khám và JSON `{"title":"...","content":"...","imageUrls":["https://example.com/photo.jpg"]}`. Trả 201, ảnh và `status: "PENDING"` (chưa duyệt); bài chỉ công khai sau khi admin duyệt. `approvedBy` và `approvedAt` đều là `null`. Tiêu đề tối đa 200 ký tự, nội dung tối đa 10000 ký tự; tối đa 10 URL HTTP(S) khác nhau, mỗi URL tối đa 2048 ký tự. `imageUrls` không bắt buộc để tương thích client cũ. Client không thể tự đặt trạng thái.
- `GET /api/v1/clinic/posts?status=ALL`: bài của phòng khám trong token, mới nhất trước. Bộ lọc `ALL` (mặc định), `PENDING`, `APPROVED`, `REJECTED` tương ứng 4 tab trên màn đăng tin. Hỗ trợ `limit=1..50`, `beforeId`; response giữ `data` là mảng và thêm `pagination` như danh sách admin. Đổi tab thì bỏ `beforeId`.
- `GET /api/v1/clinic/posts/counts`: tổng số bài của chính phòng khám theo `ALL`, `PENDING`, `APPROVED`, `REJECTED`, không giới hạn 50. Dùng `data.REJECTED` cho huy hiệu Từ chối. API lấy danh tính từ token, không cho client chọn phòng khám khác.
- `DELETE /api/v1/clinic/posts/:id`: xóa bài của phòng khám hiện tại; trả 200 với `data.id`, hoặc 404 nếu không tìm thấy bài thuộc phòng khám đó.
- `GET /api/v1/public/clinic-posts`: tối đa 50 tin mới nhất có `status: "APPROVED"` (Đã duyệt).
- `GET /api/v1/public/clinic-posts/:id`: chi tiết và toàn bộ ảnh của tin đã duyệt; tin chưa duyệt hoặc bị từ chối trả 404.
- Response bài đăng (tạo, danh sách của phòng khám, danh sách/chi tiết công khai và danh sách admin) có `clinicThumbnailUrl`: URL ảnh đại diện lấy từ hồ sơ phòng khám hiện tại (`clinics.thumbnail_url`), hoặc `null` nếu chưa có. Trường này khác `imageUrls` là ảnh của chính bài đăng.
- `GET /api/v1/admin/clinic-posts?status=PENDING`: admin xem danh sách chưa duyệt, đầy đủ nội dung và ảnh; mỗi lượt tối đa 50 bài. Thêm `limit=1..50` để đổi số bài. Response giữ `data` là mảng và thêm `pagination: {limit, hasMore, nextBeforeId}`. Khi `hasMore=true`, gọi tiếp với `beforeId=nextBeforeId`, giữ nguyên `status`; hết bài khi `hasMore=false`. Bỏ `beforeId` để tải lại từ bài mới nhất. Có thể lọc `APPROVED` (đã duyệt) hoặc `REJECTED` (từ chối duyệt). Mặc định `PENDING`.
- `GET /api/v1/admin/clinic-posts/counts`: tổng số bài theo `PENDING`, `APPROVED`, `REJECTED`, không giới hạn 50. Dùng `data.PENDING` cho huy hiệu trên tab chưa duyệt.
- `GET /api/v1/admin/clinic-posts/:id`: admin xem chi tiết bài ở cả 3 trạng thái. Có tên/ảnh đại diện phòng khám, thời gian gửi, tiêu đề, nội dung và ảnh bài. Màn chi tiết chỉ hiện Duyệt/Từ chối với `PENDING`; `APPROVED`/`REJECTED` chỉ xem. Sau thao tác, tải lại danh sách và số huy hiệu; khi gặp 409 thì tải lại chi tiết.
- `PATCH /api/v1/admin/clinic-posts/:id/approve`: access token ADMIN, không cần body. Duyệt tin `PENDING`, lưu `approvedBy`, `approvedAt` và công khai tin. Gọi duyệt lặp trả 200, giữ nguyên thông tin duyệt; tin `REJECTED` trả 409.
- `PATCH /api/v1/admin/clinic-posts/:id/reject`: access token ADMIN, không cần body. Chuyển tin `PENDING` sang `REJECTED`, không công khai. Gọi từ chối lặp trả 200; tin `APPROVED` trả 409. Các trường thông tin duyệt vẫn là `null`.

Node.js nhận `imageUrls`, không upload file. Có thể lấy URL từ API Spring Boot có sẵn `POST /api/v1/media/upload` (multipart field `file`), rồi gửi URL tới Node.js. Thứ tự ảnh được giữ nguyên; client có thể dùng `imageUrls[0]` làm thumbnail, chưa có trường thumbnail riêng. Cần chạy migration 004 sau 003 trước khi dùng tính năng từ chối duyệt.

### Kiểm tra duyệt bài bằng Swagger/Postman

1. Đăng nhập Spring Boot bằng tài khoản CLINIC để lấy access token.
2. Mở `/api-docs/`, chọn **Authorize** và điền token vào `clinicBearer`.
3. Tạo tin bằng `POST /api/v1/clinic/posts`, dùng `imageUrls` trong ví dụ Swagger. Tin mới có `status=PENDING`, `approvedBy=null`, `approvedAt=null` và chưa công khai.
4. Đăng nhập ADMIN, điền token vào `adminBearer`, xem danh sách `PENDING` rồi gọi `/approve` hoặc `/reject`. Chỉ tin `APPROVED` xuất hiện trong danh sách và chi tiết công khai.
5. Xem danh sách `REJECTED` để kiểm tra tin bị từ chối; phòng khám vẫn xem được trạng thái trong danh sách bài của mình.

Postman có folder **05 - Node.js - Duyệt bài phòng khám** chạy trọn luồng, gồm kiểm tra quyền, validation, trạng thái và xóa bài thử nghiệm. Xem [hướng dẫn Postman](../postman/README.md). Phạm vi tính năng là API Node.js; không bao gồm giao diện.

### API khác

- `GET /api/v1/admin/statistics`: yêu cầu Bearer access token `ADMIN`. Trả `data.activeClinics` (tài khoản `CLINIC` ở trạng thái `ACTIVE`, đã đổi mật khẩu mặc định và hoàn tất hồ sơ), `data.inactiveClinics` (tài khoản `CLINIC` ở trạng thái khác `ACTIVE`, gồm `PENDING_PASSWORD_CHANGE`, `PENDING_PROFILE` và `NULL`) và `data.totalUsers` (chỉ tài khoản vai trò `USER`). Ví dụ: `{"activeClinics":3,"inactiveClinics":4,"totalUsers":12}`.
- `GET /health/live` (hoặc `/health`): trả 200 khi tiến trình xử lý HTTP được.
- `GET /health/ready`: trả 200 khi truy vấn kiểm tra MySQL thành công, 503 khi database không sẵn sàng.

JWT được kiểm tra chữ ký, hạn dùng, loại access token, vai trò và danh sách token bị vô hiệu hóa trong database. Các request lỗi dùng cùng định dạng response và không trả stack trace/chi tiết database cho client.

## Tài khoản và dữ liệu kiểm thử

Chạy `npm run seed:test` khi MySQL dev trong `.env` có thể truy cập được từ nơi chạy lệnh. Script chỉ nhận `NODE_ENV` khác `production` và `DB_HOST` là `localhost`, `127.0.0.1` hoặc `devmysql`. Có thể chạy `npm run seed:test -- --prepare` để tạo file mật khẩu trước khi kết nối database. File `.env.seed.local` được Git ignore; giữ file này để chạy seed lại với cùng mật khẩu.

Nếu MySQL chỉ truy cập được từ container, chạy seed trong môi trường có kết nối tới MySQL. Không đưa mật khẩu seed vào repository.

Seed tạo `cmw_test_admin`, một phòng khám `ACTIVE` có hồ sơ và hai dịch vụ, hai tài khoản phòng khám lần lượt ở `PENDING_PASSWORD_CHANGE`/`PENDING_PROFILE`, cùng hai tài khoản `USER`. Script cập nhật các tài khoản seed khi chạy lại mà không tạo trùng; nếu username hoặc email trùng tài khoản không thuộc seed, script dừng và rollback. Mật khẩu admin, phòng khám và người dùng được sinh ngẫu nhiên và băm bằng bcrypt. Đọc `.env.seed.local` trên máy dev để lấy mật khẩu; không commit hoặc dùng các tài khoản này trên production.

Để thử thống kê, dùng `cmw_test_admin` đăng nhập tại Spring Boot `POST /api/v1/auth/login`, rồi gửi access token nhận được tới Node.js `GET /api/v1/admin/statistics`. Các số trả về bao gồm cả tài khoản đã có trước khi seed.
