const response = (description, dataSchema) => ({
  description,
  content: {
    'application/json': {
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

export const openApiDocument = {
  openapi: '3.0.3',
  info: {
    title: 'ComeMyWay Clinic Posts API',
    version: '1.0.0',
    description: 'API đăng tin phòng khám. Lấy access token CLINIC từ API đăng nhập Spring Boot.',
  },
  servers: [{ url: '/', description: 'Cùng host và cổng với Node.js' }],
  tags: [
    { name: 'Health', description: 'Trạng thái tiến trình và kết nối database' },
    { name: 'Clinic posts', description: 'Đăng tin và xem tin phòng khám' },
  ],
  components: {
    securitySchemes: {
      clinicBearer: { type: 'http', scheme: 'bearer', bearerFormat: 'JWT' },
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
          title: { type: 'string', minLength: 1, maxLength: 200, example: 'Lịch khám thú cưng' },
          content: { type: 'string', minLength: 1, maxLength: 10000, example: 'Phòng khám nhận lịch khám từ thứ Hai đến thứ Sáu.' },
        },
      },
      ClinicPost: {
        type: 'object',
        required: ['id', 'clinicId', 'clinicName', 'title', 'content', 'createdAt'],
        properties: {
          id: { type: 'integer', format: 'int64', example: 10 },
          clinicId: { type: 'integer', format: 'int64', example: 1 },
          clinicName: { type: 'string', example: 'Phòng khám thú y' },
          title: { type: 'string', example: 'Lịch khám thú cưng' },
          content: { type: 'string', example: 'Phòng khám nhận lịch khám từ thứ Hai đến thứ Sáu.' },
          createdAt: { type: 'string', format: 'date-time' },
        },
      },
    },
  },
  paths: {
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
        description: 'Phòng khám được lấy từ access token; thời gian đăng do server tạo.',
        security: [{ clinicBearer: [] }],
        requestBody: {
          required: true,
          content: { 'application/json': { schema: { $ref: '#/components/schemas/CreateClinicPost' } } },
        },
        responses: {
          201: response('Đăng tin thành công', post),
          400: errorResponse('Tiêu đề hoặc nội dung không hợp lệ'),
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
        description: 'Trả tối đa 50 tin mới nhất.',
        responses: {
          200: response('Danh sách tin', posts),
          500: errorResponse('Lỗi xử lý phía server'),
        },
      },
    },
  },
};
