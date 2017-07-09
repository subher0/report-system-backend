package com.texnoprom.controllers;

/**
 * Created by farid on 5/19/17.
 */
public class RegistrationErrors {
  public static final int GOOD = 0;
  public static final int BAD = 1;
  public static final int ACCESS_DENIED = 2;
  public static final int EMPTY_FIELDS = 3;
  public static final int WRONG_LOGIN_PASSWORD = 4;
  public static final int EXISTING_USER = 5;
  public static final int WRONG_ID = 6;

  public static String getErrorMessage(int code) {
    final String message;

    switch (code) {
      case 0: message = "Everything is ok"; break;
      case 1: message = "Bad request"; break;
      case 2: message = "You don't have the permission to do that"; break;
      case 3: message = "Some of the required fields are empty"; break;
      case 4: message = "Wrong login or password"; break;
      case 5: message = "User already exists"; break;
      case 6: message = "User under given ID does not exist"; break;
      default: message = "Unknown error"; break;
    }

    return "{\"errorCode\":\"" + code + "\",\"message\":\"" + message + "\"}";
  }
}