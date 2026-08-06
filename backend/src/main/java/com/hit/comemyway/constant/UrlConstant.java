package com.hit.comemyway.constant;

public class UrlConstant {
  public static class Public {
    private static final String PREFIX = "/public";

    public static final String CLINIC_SEARCH = PREFIX + "/clinics/search";
    public static final String CLINIC_SUGGESTION = PREFIX + "/clinics/suggestions";
    public static final String CLINIC_DETAIL = PREFIX + "/clinics/{clinicId}";
    public static final String CLINIC_BOOKING = PREFIX + "/clinics/{clinicId}/booking";
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
    public static final String UPDATE_PROFILE = PREFIX + "/update-profile/{id}";
    public static final String CHANGE_PASSWORD = PREFIX + "/change-password";
    public static final String LOGOUT = PREFIX + "/logout";
    public static final String BOOK_APPOINTMENT = PREFIX + "/appointments/book";
    public static final String DEVICE_TOKEN = PREFIX + "/device-token";
    public static final String USER_REMINDER = PREFIX + "/user-reminder";
    public static final String USER_LOCKET_LINK = PREFIX + "/locket-link";

    private User() {}
  }

  public static class Admin {
    private static final String PREFIX = "/admin";

    public static final String GET_USER = PREFIX + "/user/{userId}";
    public static final String CREATE_USER = PREFIX + "/create-user";
    public static final String UPDATE_USER = PREFIX + "/update-user/{userId}";
    public static final String DELETE_USER = PREFIX + "/delete-user/{userId}";
    public static final String CREATE_CLINIC = PREFIX + "/create-clinic";

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
    public static final String COMPLETE_PROFILE = PREFIX + "/complete-profile";
    public static final String UPDATE_PROFILE = PREFIX + "/update-profile";
    public static final String GET_PROFILE = PREFIX + "/profile";
    public static final String CHANGE_PASSWORD = PREFIX + "/change-password";
    public static final String GET_APPOINTMENT_PENDING = PREFIX + "/appointments/pending";
    public static final String POST_APPOINTMENT_CONFIRMED =
        PREFIX + "/appointments/{appointmentId}/confirm";
    public static final String POST_APPOINTMENT_REJECTED =
        PREFIX + "/appointments/{appointmentId}/reject";
    public static final String GET_APPOINTMENT_COMFIRMED = PREFIX + "/appointments/comfirmed";
    public static final String GET_APPOINTMENT_REJECTED = PREFIX + "/appointments/rejected";

    private Clinic() {}
  }

  public static class Appointment {
    private static final String USE_PREFIX = "/user/appointments";

    private static final String CLINIC_PREFIX = "/clinic/appointments";

    public static final String CREATE_APPOINTMENT = USE_PREFIX;
    public static final String GET_APPOINTMENT = USE_PREFIX;
    public static final String GET_DETAIL = USE_PREFIX + "/detail/{appointmentId}";
    public static final String UPDATE_APPOINTMENT = USE_PREFIX + "/update/{appointmentId}";
    public static final String CANCEL_APPOINTMENT = USE_PREFIX + "/cancel/{appointmentId}";

    public static final String CLINIC_GET_DETAIL = CLINIC_PREFIX + "/detail/{appointmentId}";

    private Appointment() {}
  }

  public static class Locket {
    private static final String PREFIX = "/user/locket";

    public static final String ADD_FRIEND = PREFIX + "/add-friend";
    public static final String PENDING = PREFIX + "/pending";
    public static final String MY_FRIEND = PREFIX + "/my-friend";
    public static final String ACCEPT_REQUEST = PREFIX + "/accept-request/{requestId}";
    public static final String REJECT_REQUEST = PREFIX + "/reject-request/{requestId}";
    public static final String CREATE_POST = PREFIX + "/create-post";
    public static final String NEWS_FEED = PREFIX + "/feed";
    public static final String MY_POSTS = PREFIX + "/my-posts";
    public static final String FIND_FRIEND = PREFIX + "/find-friend/{locketLink}";
    public static final String UNFRIEND = PREFIX + "/unfriend/{friendId}";
    public static final String DELETE_POST = PREFIX + "/delete-post/{postId}";

    public Locket() {}
  }
}
