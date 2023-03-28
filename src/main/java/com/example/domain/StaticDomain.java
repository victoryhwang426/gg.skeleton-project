package com.example.domain;

import lombok.Getter;
import lombok.ToString;

public class StaticDomain {
  @ToString
  @Getter
  public static class UserByYearAndMonth {
    private final Long userId;
    private final String userName;
    private final int year;
    private final int month;
    private final long totalAmount;

    public UserByYearAndMonth(
      long userId,
      String userName,
      int year,
      int month,
      long totalAmount
    ) {
      this.userId = userId;
      this.userName = userName;
      this.year = year;
      this.month = month;
      this.totalAmount = totalAmount;
    }
  }

  @ToString
  @Getter
  public static class ProductByYearAndMonth {
    private final Long productId;
    private final String productName;
    private final int year;
    private final int month;
    private final long totalAmount;
    private final int totalUserCount;

    public ProductByYearAndMonth(
      long productId,
      String productName,
      int year,
      int month,
      long totalAmount,
      int totalUserCount
    ) {
      this.productId = productId;
      this.productName = productName;
      this.year = year;
      this.month = month;
      this.totalAmount = totalAmount;
      this.totalUserCount = totalUserCount;
    }
  }
}
