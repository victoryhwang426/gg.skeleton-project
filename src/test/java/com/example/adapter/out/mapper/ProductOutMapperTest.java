package com.example.adapter.out.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.UnitTest;
import com.example.domain.ProductDomain.ProductInfo;
import com.example.domain.ProductDomain.RegisterProductCommand;
import com.example.infra.database.Product;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class ProductOutMapperTest extends UnitTest{
  @InjectMocks
  private ProductOutMapper productOutMapper;

  @Nested
  class makeProduct {
    @Test
    @DisplayName("상품등록 커맨드를 이용하여 상품 신규 엔티티를 생성한다")
    void test1() {
      String productName = RandomString.make(10);
      Integer price = 123123;

      RegisterProductCommand command = new RegisterProductCommand(productName, price);
      Product result = productOutMapper.makeProduct(command);

      assertThat(result.getProductId()).isNull();
      assertThat(result).usingRecursiveComparison()
        .ignoringFields("productId")
        .isEqualTo(command);
    }
  }

  @Nested
  class toProductInfo {
    @Test
    @DisplayName("엔티티를 상품정보 도메인으로 변환한다")
    void test1(){
      Product entity = Product.builder()
        .productId(123123L)
        .productName(RandomString.make(19))
        .price(1212)
        .build();
      ProductInfo result = productOutMapper.toProductInfo(entity);

      assertThat(result).usingRecursiveComparison()
        .isEqualTo(entity);
    }
  }
}