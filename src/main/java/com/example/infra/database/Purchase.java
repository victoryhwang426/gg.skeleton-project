package com.example.infra.database;

import com.example.domain.PurchaseDomain.RegisterPurchaseCommand;
import com.example.infra.database.common.BaseTime;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class Purchase extends BaseTime {
  @Id
  @Column(name = "purchase_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long purchaseId;

  @Column(name = "purchase_no")
  private Long purchaseNumber;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @Column(name = "price")
  private Integer price;

  private int yearOfCreatedAt;

  public Purchase(RegisterPurchaseCommand command) {
    this.user = User.builder()
      .userId(command.getUserId())
      .build();
    this.product = Product.builder()
      .productId(command.getProductId())
      .build();
    this.price = command.getPrice();
    this.yearOfCreatedAt = command.getYearOfCreatedAt();
  }
}
