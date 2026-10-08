const response = (description, dataSchema, example) => ({
  description,
  content: {
    'application/json': {
      ...(example ? { example: { statusCode: 200, message: description, data: example,
        timestamp: '2026-10-03T08:00:00.000Z' } } : {}),
      schema: {
        allOf: [
          { $ref: '#/components/schemas/ApiResponse' },
          { type: 'object', properties: { data: dataSchema } },
        ],
      },
    },
  },
});

const errorResponse = description => response(description, { nullable: true, example: null });
const post = { $ref: '#/components/schemas/ClinicPost' };
const posts = { type: 'array', items: post };
const imageUrls = { type: 'array', maxItems: 10, uniqueItems: true,
  items: { type: 'string', format: 'uri', maxLength: 2048 },
  description: 'Tối đa 10 URL HTTP(S), không trùng. Giữ nguyên thứ tự; client có thể dùng ảnh đầu làm thumbnail. Node.js nhận URL, không nhận file multipart.',
  example: ['https://res.cloudinary.com/demo/image/upload/sample.jpg'], default: [] };
const pendingExample = { id: 10, clinicId: 1, clinicName: 'Phòng khám thú y',
  clinicThumbnailUrl: 'https://example.com/clinic-avatar.jpg',
  title: 'Lịch tiêm phòng', content: 'Nhận lịch tiêm phòng cho thú cưng.',
  imageUrls: imageUrls.example, status: 'PENDING', approvedBy: null, approvedAt: null,
  createdAt: '2026-10-03T07:00:00.000Z' };
const approvedExample = { ...pendingExample, status: 'APPROVED', approvedBy: 2,
  approvedAt: '2026-10-03T08:00:00.000Z' };
const autoApprovedExample = { ...pendingExample, status: 'APPROVED', approvedBy: null,
  approvedAt: pendingExample.createdAt };
const postId = { in: 'path', name: 'id', required: true,
  schema: { type: 'integer', format: 'int64', minimum: 1, maximum: Number.MAX_SAFE_INTEGER } };

export const openApiDocument = {
  openapi: '3.0.3',
  info: {
    title: 'ComeMyWay Node.js API',
    version: '1.0.0',
    description: 'API đăng tin có ảnh, admin xử lý tin cũ chờ duyệt và thống kê. Lấy access token từ Spring Boot, chọn Authorize đúng vai trò. Tin mới tự động APPROVED và công khai ngay; approvedBy là null, approvedAt là thời điểm đăng. Chạy migration 001, 002 và 003 trước khi sử dụng.',
  },
  servers: [{ url: '/', description: 'Cùng host và cổng với Node.js' }],
  tags: [
    { name: 'Health', description: 'Trạng thái tiến trình và kết nối database' },
    { name: 'Clinic posts', description: 'Đăng tin và xem tin phòng khám' },
    { name: 'Admin', description: 'Duyệt bài phòng khám và thống kê tài khoản' },
  ],
  components: {
    securitySchemes: {
      clinicBearer: { type: 'http', scheme: 'bearer', bearerFormat: 'JWT',
        description: 'Access token vai trò CLINIC từ API đăng nhập Spring Boot.' },
      adminBearer: { type: 'http', scheme: 'bearer', bearerFormat: 'JWT',
        description: 'Access token vai trò ADMIN từ API đăng nhập Spring Boot.' },
    },
    schemas: {
      ApiResponse: {
        type: 'object',
        required: ['statusCode', 'message', 'data', 'timestamp'],
        properties: {
          statusCode: { type: 'integer', example: 200 },
          message: { type: 'string', example: 'OK' },
          data: { nullable: true },
          timestamp: { type: 'string', format: 'date-time' },
        },
      },
      CreateClinicPost: {
        type: 'object',
        required: ['title', 'content'],
        additionalProperties: false,
        properties: {
          imageUrls,
          title: { type: 'string', minLength: 1, maxLength: 200, example: 'Lịch khám thú cưng' },
          content: { type: 'string', minLength: 1, maxLength: 10000, example: 'Phòng khám nhận lịch khám từ thứ Hai đến thứ Sáu.' },
        },
      },
      ClinicPost: {
        type: 'object',
        required: ['id', 'clinicId', 'clinicName', 'clinicThumbnailUrl', 'title', 'content', 'createdAt', 'imageUrls', 'status'],
        properties: {
          imageUrls,
          clinicThumbnailUrl: { type: 'string', format: 'uri', nullable: true,
            description: 'URL ảnh đại diện phòng khám từ clinics.thumbnail_url; null nếu phòng khám chưa có ảnh.',
            example: 'https://example.com/clinic-avatar.jpg' },
          status: { type: 'string', enum: ['PENDING', 'APPROVED'], description: 'Tin mới tự động APPROVED; PENDING chỉ còn ở tin cũ chưa duyệt.' },
          approvedBy: { type: 'integer', format: 'int64', nullable: true, description: 'null khi tự động duyệt; ID admin khi duyệt tin cũ.' },
          approvedAt: { type: 'string', format: 'date-time', nullable: true, description: 'Thời điểm đăng khi tự động duyệt; thời điểm admin duyệt với tin cũ.' },
          id: { type: 'integer', format: 'int64', example: 10 },
          clinicId: { type: 'integer', format: 'int64', example: 1 },
          clinicName: { type: 'string', example: 'Phòng khám thú y' },
          title: { type: 'string', example: 'Lịch khám thú cưng' },
          content: { type: 'string', example: 'Phòng khám nhận lịch khám từ thứ Hai đến thứ Sáu.' },
          createdAt: { type: 'string', format: 'date-time' },
        },
      },
      AdminStatistics: {
        type: 'object',
        required: ['activeClinics', 'inactiveClinics', 'totalUsers'],
        properties: {
          activeClinics: { type: 'integer', minimum: 0, example: 3,
            description: 'Tài khoản CLINIC đã đổi mật khẩu mặc định: PENDING_PROFILE hoặc ACTIVE, không yêu cầu hoàn tất hồ sơ.' },
          inactiveClinics: { type: 'integer', minimum: 0, example: 4,
            description: 'Tài khoản CLINIC chưa xác nhận đã đổi mật khẩu mặc định: PENDING_PASSWORD_CHANGE, NULL hoặc trạng thái ngoài PENDING_PROFILE/ACTIVE.' },
          totalUsers: { type: 'integer', minimum: 0, example: 12,
            description: 'Tài khoản có vai trò USER; không gồm ADMIN và CLINIC.' },
        },
      },
    },
  },
  paths: {
    '/api/v1/admin/clinic-posts': {
      get: {
        tags: ['Admin'], summary: 'Danh sách tin theo trạng thái', security: [{ adminBearer: [] }],
        operationId: 'listClinicPostsForModeration',
        parameters: [{ in: 'query', name: 'status', schema: { type: 'string', enum: ['PENDING', 'APPROVED'], default: 'PENDING' } }],
        description: 'Tối đa 50 tin mới nhất theo trạng thái. Tin mới tự động APPROVED; PENDING chỉ dùng cho tin cũ chưa duyệt.',
        responses: { 200: response('Danh sách tin', posts, [pendingExample]), 400: errorResponse('Trạng thái không hợp lệ'),
          401: errorResponse('Thiếu token, token hết hạn, refresh token hoặc sai vai trò ADMIN'),
          403: errorResponse('Tài khoản ADMIN không tồn tại hoặc token bị thu hồi'),
          500: errorResponse('Lỗi xử lý phía server') },
      },
    },
    '/api/v1/admin/clinic-posts/{id}/approve': {
      patch: {
        tags: ['Admin'], summary: 'Duyệt tin phòng khám', security: [{ adminBearer: [] }], parameters: [postId],
        operationId: 'approveClinicPost',
        description: 'Chỉ cần cho tin cũ đang PENDING. Không cần request body. Chuyển PENDING thành APPROVED, lấy admin từ token và thời gian từ server. Gọi với tin đã tự động duyệt trả 200, giữ nguyên approvedBy=null và approvedAt ban đầu. Phòng khám không thể tự đặt trạng thái khi tạo tin.',
        responses: { 200: response('Đã duyệt', post, approvedExample), 400: errorResponse('ID phải là số nguyên dương an toàn'),
          401: errorResponse('Thiếu token, token hết hạn, refresh token hoặc sai vai trò ADMIN'),
          403: errorResponse('Tài khoản ADMIN không tồn tại hoặc token bị thu hồi'),
          404: errorResponse('Không tìm thấy bài đăng'), 500: errorResponse('Lỗi xử lý phía server') },
      },
    },
    '/api/v1/public/clinic-posts/{id}': {
      get: {
        tags: ['Clinic posts'], summary: 'Chi tiết tin đã duyệt', parameters: [postId],
        operationId: 'getPublicClinicPost', security: [],
        responses: { 200: response('Chi tiết tin', post, approvedExample), 400: errorResponse('ID không hợp lệ'),
          404: errorResponse('Tin không tồn tại hoặc chưa được duyệt'), 500: errorResponse('Lỗi xử lý phía server') },
      },
    },
    '/api/v1/admin/statistics': {
      get: {
        tags: ['Admin'], summary: 'Thống kê tài khoản phòng khám và người dùng',
        description: 'Đang hoạt động nghĩa là đã đổi mật khẩu mặc định do admin cấp: tính cả PENDING_PROFILE và ACTIVE. PENDING_PASSWORD_CHANGE, NULL hoặc trạng thái ngoài hai trạng thái trên được tính là chưa hoạt động. Tổng người dùng chỉ tính vai trò USER. Yêu cầu access token ADMIN từ Spring Boot.',
        security: [{ adminBearer: [] }],
        responses: {
          200: response('Thống kê tài khoản', { $ref: '#/components/schemas/AdminStatistics' }),
          401: errorResponse('Thiếu token, token không hợp lệ hoặc không mang vai trò ADMIN'),
          403: errorResponse('Tài khoản ADMIN không tồn tại hoặc token đã bị thu hồi'),
          500: errorResponse('Lỗi xử lý phía server'),
        },
      },
    },
    '/health': {
      get: {
        tags: ['Health'], summary: 'Kiểm tra tiến trình HTTP',
        responses: { 200: response('Tiến trình hoạt động', { type: 'object', properties: { status: { type: 'string', example: 'ok' } } }) },
      },
    },
    '/health/live': {
      get: {
        tags: ['Health'], summary: 'Liveness',
        responses: { 200: response('Tiến trình hoạt động', { type: 'object', properties: { status: { type: 'string', example: 'ok' } } }) },
      },
    },
    '/health/ready': {
      get: {
        tags: ['Health'], summary: 'Kiểm tra kết nối MySQL',
        responses: {
          200: response('Sẵn sàng', { type: 'object', properties: { status: { type: 'string', example: 'ready' } } }),
          503: response('Database chưa sẵn sàng', { type: 'object', properties: { status: { type: 'string', example: 'not_ready' } } }),
        },
      },
    },
    '/api/v1/clinic/posts': {
      post: {
        tags: ['Clinic posts'], summary: 'Phòng khám đăng tin',
        description: 'Phòng khám được lấy từ access token; thời gian đăng do server tạo. Tin tự động APPROVED và công khai ngay, approvedBy=null và approvedAt là thời điểm đăng. Upload ảnh qua Spring Boot /api/v1/media/upload rồi gửi imageUrls.',
        security: [{ clinicBearer: [] }],
        requestBody: {
          required: true,
          content: { 'application/json': { schema: { $ref: '#/components/schemas/CreateClinicPost' },
            examples: {
              withImages: { summary: 'Tin có ảnh', value: { title: pendingExample.title,
                content: pendingExample.content, imageUrls: imageUrls.example } },
              textOnly: { summary: 'Tin không kèm ảnh', value: { title: pendingExample.title, content: pendingExample.content } },
            } } },
        },
        responses: {
          201: response('Đăng tin thành công', post, autoApprovedExample),
          400: errorResponse('Tiêu đề, nội dung hoặc danh sách URL ảnh không hợp lệ'),
          401: errorResponse('Thiếu hoặc sai access token'),
          403: errorResponse('Tài khoản không phải phòng khám đang hoạt động'),
          413: errorResponse('Body vượt quá 64 KB'),
          500: errorResponse('Lỗi xử lý phía server'),
        },
      },
      get: {
        tags: ['Clinic posts'], summary: 'Tin của phòng khám hiện tại',
        description: 'Trả tối đa 50 tin mới nhất.',
        security: [{ clinicBearer: [] }],
        responses: {
          200: response('Danh sách tin', posts),
          401: errorResponse('Thiếu hoặc sai access token'),
          403: errorResponse('Tài khoản không phải phòng khám đang hoạt động'),
          500: errorResponse('Lỗi xử lý phía server'),
        },
      },
    },
    '/api/v1/clinic/posts/{id}': {
      delete: {
        tags: ['Clinic posts'], summary: 'Xóa bài đăng của phòng khám hiện tại',
        description: 'Chỉ xóa bài do phòng khám trong access token đăng. Bài không tồn tại hoặc thuộc phòng khám khác đều trả 404.',
        security: [{ clinicBearer: [] }],
        parameters: [{
          in: 'path', name: 'id', required: true, description: 'ID bài đăng',
          schema: { type: 'integer', format: 'int64', minimum: 1 },
        }],
        responses: {
          200: response('Xóa bài đăng thành công', {
            type: 'object', required: ['id'], properties: { id: { type: 'integer', format: 'int64' } },
          }),
          400: errorResponse('ID bài đăng không hợp lệ'),
          401: errorResponse('Thiếu hoặc sai access token'),
          403: errorResponse('Tài khoản không phải phòng khám đang hoạt động'),
          404: errorResponse('Không tìm thấy bài đăng'),
          500: errorResponse('Lỗi xử lý phía server'),
        },
      },
    },
    '/api/v1/public/clinic-posts': {
      get: {
        tags: ['Clinic posts'], summary: 'Tin phòng khám công khai',
        description: 'Trả tối đa 50 tin mới nhất có trạng thái APPROVED, gồm tin mới được công khai tự động.',
        responses: {
          200: response('Danh sách tin', posts),
          500: errorResponse('Lỗi xử lý phía server'),
        },
      },
    },
  },
};
