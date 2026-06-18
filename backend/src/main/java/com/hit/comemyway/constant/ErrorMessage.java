package com.hit.comemyway.constant;

public final class ErrorMessage {
  private ErrorMessage() {}

  // General System Errors
  public static final String EXCEPTION_GENERAL = "ERR_SYS_GENERAL";
  public static final String UNAUTHORIZED = "ERR_SYS_UNAUTHORIZED";
  public static final String FORBIDDEN = "ERR_SYS_FORBIDDEN";
  public static final String BAD_REQUEST = "ERR_SYS_BAD_REQUEST";
  public static final String FORBIDDEN_UPDATE_DELETE = "ERR_SYS_FORBIDDEN_UPDATE_DELETE";
  public static final String UPLOAD_IMAGE_FAIL = "ERR_SYS_UPLOAD_IMAGE_FAIL";

  // Common DTO Validation Errors
  public static final String INVALID_SOME_THING_FIELD = "ERR_VAL_INVALID_FIELD";
  public static final String INVALID_FORMAT_SOME_THING_FIELD = "ERR_VAL_INVALID_FORMAT";
  public static final String INVALID_SOME_THING_FIELD_IS_REQUIRED = "ERR_VAL_REQUIRED";
  public static final String NOT_BLANK_FIELD = "ERR_VAL_NOT_BLANK";
  public static final String INVALID_FORMAT_PASSWORD = "ERR_VAL_INVALID_PASSWORD_FORMAT";
  public static final String INVALID_USERNAME_LENGTH = "ERR_VAL_INVALID_USERNAME_LENGTH";
  public static final String INVALID_FORMAT_EMAIL = "ERR_VAL_INVALID_EMAIL_FORMAT";
  public static final String PASSWORD_MISMATCH = "ERR_VAL_PASSWORD_MISMATCH";

  // Authentication Errors (Auth)
  public static final class Auth {
    private Auth() {} // Prevent instantiation

    public static final String INVALID_CREDENTIALS = "ERR_AUTH_INVALID_CREDENTIALS";
    public static final String INVALID_REFRESH_TOKEN = "ERR_AUTH_INVALID_REFRESH_TOKEN";
    public static final String EXPIRED_REFRESH_TOKEN = "ERR_AUTH_EXPIRED_REFRESH_TOKEN";
    public static final String LOGIN_FAIL = "ERR_AUTH_LOGIN_FAIL";
    public static final String GET_TOKEN_CLAIM_SET_FAIL = "ERR_AUTH_GET_TOKEN_CLAIM_FAIL";
    public static final String TOKEN_INVALIDATED = "ERR_AUTH_TOKEN_INVALIDATED";
    public static final String MALFORMED_TOKEN = "ERR_AUTH_MALFORMED_TOKEN";
    public static final String TOKEN_ALREADY_INVALIDATED = "ERR_AUTH_TOKEN_ALREADY_INVALIDATED";
    public static final String INVALID_LOGOUT_TOKEN = "ERR_AUTH_INVALID_LOGOUT_TOKEN";
  }

  // User Errors
  public static final class User {
    private User() {}

    public static final String USER_NOT_EXISTED = "ERR_USER_NOT_EXISTED";
    public static final String USERNAME_EXISTED = "ERR_USER_USERNAME_EXISTED";
    public static final String EMAIL_EXISTED = "ERR_USER_EMAIL_EXISTED";
  }

  // Administrator Errors (Admin)
  public static final class Admin {
    private Admin() {}

    public static final String NOT_ADMIN = "ERR_ADMIN_NOT_ADMIN";
  }
}