package com.hit.comemyway.constant;

public final class ErrorMessage {
  private ErrorMessage() {}

  // General System Errors
  public static final String EXCEPTION_GENERAL = "An unexpected system error occurred. Please try again later.";
  public static final String UNAUTHORIZED = "Unauthorized access. Please log in.";
  public static final String FORBIDDEN = "You do not have permission to perform this action.";
  public static final String BAD_REQUEST = "Invalid request data.";
  public static final String FORBIDDEN_UPDATE_DELETE =
          "You do not have permission to update or delete this data.";
  public static final String UPLOAD_IMAGE_FAIL = "Failed to upload image. Please try again.";

  // Common DTO Validation Errors
  public static final String INVALID_SOME_THING_FIELD = "Invalid field data.";
  public static final String INVALID_FORMAT_SOME_THING_FIELD = "Incorrect data format.";
  public static final String INVALID_SOME_THING_FIELD_IS_REQUIRED =
          "This field is required.";
  public static final String NOT_BLANK_FIELD = "This field cannot be blank.";
  public static final String INVALID_FORMAT_PASSWORD =
          "Invalid password format (minimum 8 characters, including letters and numbers).";

  // Authentication Errors (Auth)
  public static final class Auth {
    private Auth() {} // Prevent instantiation

    public static final String INVALID_CREDENTIALS = "Invalid username or password.";
    public static final String INVALID_REFRESH_TOKEN = "Invalid refresh token.";
    public static final String EXPIRED_REFRESH_TOKEN =
            "Login session has expired. Please log in again.";
    public static final String LOGIN_FAIL = "Login failed.";
    public static final String GET_TOKEN_CLAIM_SET_FAIL =
            "Error extracting token claims.";
    public static final String TOKEN_INVALIDATED = "Token has been invalidated.";
    public static final String MALFORMED_TOKEN = "Malformed token.";
    public static final String TOKEN_ALREADY_INVALIDATED =
            "This token has already been logged out or previously invalidated.";
  }

  // User Errors
  public static final class User {
    private User() {}

    public static final String USER_NOT_EXISTED = "User does not exist in the system.";
    public static final String USERNAME_EXISTED =
            "This username is already taken. Please choose another one.";
    public static final String EMAIL_EXISTED = "This email address is already registered.";
  }

  // Administrator Errors (Admin)
  public static final class Admin {
    private Admin() {}

    public static final String NOT_ADMIN =
            "Administrator privileges are required to perform this action.";
  }
}