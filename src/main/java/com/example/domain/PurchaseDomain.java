package com.example.domain;

import com.example.domain.ProductDomain.ProductInfo;
import com.example.infra.database.Purchase;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

public class PurchaseDomain {
  @Getter
  @ToString
  public static class PurchaseInfo {
    private final Long purchaseId;
    private final Long purchaseNumber;
    private final Long userId;
    private final String userName;
    private final Long productId;
    private final String productName;
    private final Integer price;
    private final int yearOfCreatedAt;
    private final int monthOfCreatedAt;

    public PurchaseInfo(Purchase entity) {
      this.purchaseId = entity.getPurchaseId();
      this.purchaseNumber = entity.getPurchaseNumber();
      this.userId = entity.getUser().getUserId();
      this.userName = entity.getUser().getUserName();
      this.productId = entity.getProduct().getProductId();
      this.productName = entity.getProduct().getProductName();
      this.price = entity.getPrice();
      this.yearOfCreatedAt = entity.getYearOfCreatedAt();
      this.monthOfCreatedAt = entity.getMonthOfCreatedAt();
    }

    public String getKeyByUserAndYearAndMonth() {
      return String.format("%s_%s_%s", userId, yearOfCreatedAt, monthOfCreatedAt);
    }

    public String getKeyByProductAndYearAndMonth() {
      return String.format("%s_%s_%s", productId, yearOfCreatedAt, monthOfCreatedAt);
    }
  }

  @Getter
  @ToString
  public static class RegisterPurchaseCommand {
    private final Long userId;
    private final Long productId;
    private final Integer price;
    private final int yearOfCreatedAt;
    private final int monthOfCreatedAt;

    public RegisterPurchaseCommand(long userId, ProductInfo productInfo){
      this.userId = userId;
      this.productId = productInfo.getProductId();
      this.price = productInfo.getPrice();

      LocalDateTime today = LocalDateTime.now();
      this.yearOfCreatedAt = today.getYear();
      this.monthOfCreatedAt = today.getMonthValue();
    }
  }
}
