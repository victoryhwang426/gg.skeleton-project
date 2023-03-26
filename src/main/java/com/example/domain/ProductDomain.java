package com.example.domain;

import com.example.infra.database.Product;
import lombok.Getter;
import lombok.ToString;

public class ProductDomain {
  @Getter
  @ToString
  public static class ProductInfo {
    private final Long productId;
    private final String productName;
    private final Integer price;

    public ProductInfo(Product entity){
      this.productId = entity.getProductId();
      this.productName = entity.getProductName();
      this.price = entity.getPrice();
    }
  }

  @Getter
  @ToString
  public static class RegisterProductCommand {
    private final String productName;
    private final Integer price;

    public RegisterProductCommand(String productName, Integer price){
      this.productName = productName;
      this.price = price;
    }
  }

  @Getter
  @ToString
  public static class ModifyProductCommand {
    private final long productId;
    private final String productName;
    private final Integer price;

    public ModifyProductCommand(long productId, String productName, Integer price){
      this.productId = productId;
      this.productName = productName;
      this.price = price;
    }
  }
}
