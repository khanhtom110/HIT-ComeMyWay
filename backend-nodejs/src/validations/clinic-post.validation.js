import Joi from 'joi';
import { CLINIC_POST_LIMITS } from '../constants/clinic-post.constant.js';

export const createClinicPostValidation = {
  body: Joi.object({
    title: Joi.string().trim().min(1).max(CLINIC_POST_LIMITS.TITLE_LENGTH).required()
      .messages({
        'any.required': 'Tiêu đề là bắt buộc',
        'string.base': 'Tiêu đề phải là chuỗi',
        'string.empty': 'Tiêu đề là bắt buộc',
        'string.max': 'Tiêu đề tối đa 200 ký tự',
      }),
    content: Joi.string().trim().min(1).max(CLINIC_POST_LIMITS.CONTENT_LENGTH).required()
      .messages({
        'any.required': 'Nội dung là bắt buộc',
        'string.base': 'Nội dung phải là chuỗi',
        'string.empty': 'Nội dung là bắt buộc',
        'string.max': 'Nội dung tối đa 10000 ký tự',
      }),
  }).required(),
};

export const deleteClinicPostValidation = {
  params: Joi.object({
    id: Joi.number().integer().positive().max(Number.MAX_SAFE_INTEGER).required(),
  }).required(),
};
