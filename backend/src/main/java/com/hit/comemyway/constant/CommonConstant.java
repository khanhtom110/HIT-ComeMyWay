package com.hit.comemyway.constant;

public final class CommonConstant {
  public static final int USERNAME_LENGTH = 120;
  public static final int PASSWORD_LENGTH = 120;
  public static final int FULLNAME_LENGTH = 120;
  public static final int PHONE_LENGTH = 15;
  public static final String PHONE_REGEX = "^0[35789]\\d{8}$";
  public static final int EMAIL_LENGTH = 100;
  public static final int ADDRESS_LENGTH = 255;
  public static final int CONDITION_LENGTH = 1000;

  public static final class Clinic {
    public static final int NAME_LENGTH = 120;
    public static final int THUMBNAIL_LENGTH = 500;
    public static final int REJECT_REASON_LENGTH = 500;
  }

  public static final class User {
    public static final int AVATAR_LENGTH = 500;
    public static final int HOBBY_LENGTH = 300;
    public static final int FRIENDSHIP_STATUS_LENGTH = 20;
    public static final int IMAGE_URL_LENGTH = 1000;
  }

  public static final class Locket {
    public static final int LIMIT_FRIEND = 20;
    public static final int LIMIT_LENGTH_CAPTION = 50;
  }
}
