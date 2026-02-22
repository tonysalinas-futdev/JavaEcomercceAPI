package com.example.ecommerce.shared.utils;

public class PageableUtils {
  public static Integer verifySize(Integer size) {
    Integer maxSize = 100;
    if (size == null) {
      return 10;
    }

    if (size > maxSize) {
      return maxSize;
    }

    return size;
  }

  public static Integer verifyPage(Integer page) {
    if (page == null) {
      return 1;
    }
    return page;
  }
}
