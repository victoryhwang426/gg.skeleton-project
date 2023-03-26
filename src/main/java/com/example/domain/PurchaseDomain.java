package com.example.domain;

import com.example.infra.database.Purchase;
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

    public PurchaseInfo(Purchase entity) {
      this.purchaseId = entity.getPurchaseId();
      this.purchaseNumber = entity.getPurchaseNumber();
      this.userId = entity.getUser().getUserId();
      this.productId = entity.getProduct().getProductId();
      this.price = entity.getPrice();
    }
  }
}
