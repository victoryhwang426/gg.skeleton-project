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
    private final Long productId;
    private final Integer price;
    private final int yearOfCreatedAt;

    public PurchaseInfo(Purchase entity) {
      this.purchaseId = entity.getPurchaseId();
      this.purchaseNumber = entity.getPurchaseNumber();
      this.userId = entity.getUser().getUserId();
      this.productId = entity.getProduct().getProductId();
      this.price = entity.getPrice();
      this.yearOfCreatedAt = entity.getYearOfCreatedAt();
    }
  }

  @Getter
  @ToString
  public static class RegisterPurchaseCommand {
    private final Long userId;
    private final Long productId;
    private final Integer price;
    private final int yearOfCreatedAt;

    public RegisterPurchaseCommand(long userId, ProductInfo productInfo){
      this.userId = userId;
      this.productId = productInfo.getProductId();
      this.price = productInfo.getPrice();
      this.yearOfCreatedAt = LocalDateTime.now().getYear();
    }
  }
}
