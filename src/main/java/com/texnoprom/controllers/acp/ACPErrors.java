package com.texnoprom.controllers.acp;

public class ACPErrors {
  public static final int GOOD = 0;
  public static final int BAD = 1;
  public static final int ACCESS_DENIED = 2;
  public static final int EMPTY_FIELDS = 3;

  public static String getErrorMessage(int code) {
    final String message;

    switch (code) {
      case 0: message = "Everything is ok"; break;
      case 1: message = "Bad request, probably wrong id"; break;
      case 2: message = "You don't have the permission to do that"; break;
      case 3: message = "Please enter the id"; break;
      default: message = "Unknown error!";
    }

    return "{\"errorCode\":\"" + code + "\",\"message\":\"" + message + "\"}";
  }
}
