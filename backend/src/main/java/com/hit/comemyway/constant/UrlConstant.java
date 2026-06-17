package com.hit.comemyway.constant;

public class UrlConstant {
  public static class Auth {
    private static final String PREFIX = "/auth";

    public static final String LOGIN = PREFIX + "/login";
    public static final String REFRESH_TOKEN = PREFIX + "/refresh";
    public static final String LOGOUT = PREFIX + "/logout";

    private Auth() {}
  }

  public static class User {
    private static final String PREFIX = "/user";

    public static final String GET_PROFILE = PREFIX + "/profile";

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
}
