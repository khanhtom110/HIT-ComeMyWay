package com.hit.comemyway.constant;

public class UrlConstant {
  public static class Public {
    private static final String PREFIX = "/public";

    public static final String CLINIC_SEARCH = PREFIX + "/clinics/search";
    public static final String CLINIC_SUGGESTION = PREFIX + "/clinics/suggestions";
    public static final String CLINIC_DETAIL = PREFIX + "/clinics/{clinicId}";
    public static final String GET_SUGGESTION = PREFIX + "/suggestions/location";
    public static final String HEALTH_CHECK = PREFIX + "/health";

    private Public() {}
  }

  public static class Auth {
    private static final String PREFIX = "/auth";

    public static final String LOGIN = PREFIX + "/login";
    public static final String REGISTER = PREFIX + "/register";
    public static final String REFRESH_TOKEN = PREFIX + "/refresh";
    public static final String FORGOT_PASSWORD = PREFIX + "/forgot-password";
    public static final String VERIFY_OTP = PREFIX + "/verify-otp";
    public static final String VERIFY_REGISTER_OTP = PREFIX + "/verify-register";
    public static final String RESET_PASSWORD = PREFIX + "/reset-password";

    private Auth() {}
  }

  public static class User {
    private static final String PREFIX = "/user";

    public static final String GET_PROFILE = PREFIX + "/profile";
    public static final String LOGOUT = PREFIX + "/logout";
    public static final String BOOK_APPOINTMENT = PREFIX + "/appointments/book";

    private User() {}
  }

  public static class Admin {
    private static final String PREFIX = "/admin";

    public static final String GET_USER = PREFIX + "/user/{userId}";
    public static final String CREATE_USER = PREFIX + "/create-user";
    public static final String UPDATE_USER = PREFIX + "/update-user/{userId}";
    public static final String DELETE_USER = PREFIX + "/delete-user/{userId}";

    private Admin() {}
  }

  public static class Media {
    private static final String PREFIX = "/media";

    public static final String UPLOAD = PREFIX + "/upload";

    private Media() {}
  }

  public static class Clinic {
    private static final String PREFIX = "/clinic";

    public static final String GET_DETAIL = PREFIX + "/{clinicId}";
    public static final String GET_ALL = PREFIX;

    private Clinic() {}
  }
}
