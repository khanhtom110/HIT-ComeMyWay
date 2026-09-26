import { HttpError } from '../../http/http-error.js';

export function validateClinicPost(input) {
  const title = typeof input?.title === 'string' ? input.title.trim() : '';
  const content = typeof input?.content === 'string' ? input.content.trim() : '';
  if (!title || title.length > 200 || !content || content.length > 10000) {
    throw new HttpError(400, 'Tiêu đề (1–200 ký tự) và nội dung (1–10000 ký tự) là bắt buộc');
  }
  return { title, content };
}
