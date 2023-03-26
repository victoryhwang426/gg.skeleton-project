package com.example.infra.database;

import com.example.domain.ProductDomain.ModifyProductCommand;
import com.example.domain.ProductDomain.RegisterProductCommand;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
  @Id
  @Column(name = "product_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long productId;

  @Column(name = "product_name")
  private String productName;

  @Column(name = "price")
  private Integer price;

  public Product(RegisterProductCommand command) {
    this.productName = command.getProductName();
    this.price = command.getPrice();
  }

  public void updateProduct(ModifyProductCommand command){
    this.productName = command.getProductName();
    this.price =  command.getPrice();
  }
}
