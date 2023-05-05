package com.example.adapter.out.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.UnitTest;
import com.example.domain.ProductDomain.ProductInfo;
import com.example.domain.PurchaseDomain.PurchaseInfo;
import com.example.domain.PurchaseDomain.RegisterPurchaseCommand;
import com.example.infra.database.Product;
import com.example.infra.database.Purchase;
import com.example.infra.database.User;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class PurchaseOutMapperTest extends UnitTest {

  @InjectMocks
  private PurchaseOutMapper purchaseOutMapper;
  private final Product PRODUCT = Product.builder()
      .productId(1231257123L)
      .productName(RandomString.make(20))
      .price(1231283712)
      .build();

  @Nested
  class makePurchase {

    @Test
    @DisplayName("create new purchase entity")
    void test1() {
      long userId = 123719275L;
      ProductInfo productInfo = new ProductInfo(PRODUCT);
      RegisterPurchaseCommand command = new RegisterPurchaseCommand(userId, productInfo);

      Purchase result = purchaseOutMapper.makePurchase(command);

      assertThat(result.getPurchaseId()).isNull();
      assertThat(result.getUser().getUserId()).isEqualTo(command.getUserId());
      assertThat(result.getProduct().getProductId()).isEqualTo(command.getProductId());
      assertThat(result).usingRecursiveComparison()
          .ignoringFields("purchaseId", "purchaseNumber", "user", "product", "createdAt",
              "modifiedAt")
          .isEqualTo(command);
    }
  }

  @Nested
  class toPurchaseInfo {

    @Test
    @DisplayName("convert purchase entity to purchase info domain")
    void test2() {
      Purchase entity = Purchase.builder()
          .purchaseId(123123L)
          .price(1231231)
          .user(User.builder()
              .userId(123918273L)
              .userName(RandomString.make(10))
              .build())
          .product(PRODUCT)
          .build();
      PurchaseInfo result = purchaseOutMapper.toPurchaseInfo(entity);

      assertThat(result.getUserId()).isEqualTo(entity.getUser().getUserId());
      assertThat(result.getUserName()).isEqualTo(entity.getUser().getUserName());
      assertThat(result.getProductId()).isEqualTo(entity.getProduct().getProductId());
      assertThat(result.getProductName()).isEqualTo(entity.getProduct().getProductName());
      assertThat(result).usingRecursiveComparison()
          .ignoringFields("userId", "userName", "productId", "productName")
          .isEqualTo(entity);
    }
  }
}