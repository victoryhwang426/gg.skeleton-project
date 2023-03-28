package com.example.adapter.in.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.UnitTest;
import com.example.adapter.in.dto.PurchaseDto.BuyProduct;
import com.example.adapter.in.mapper.PurchaseMapper;
import com.example.domain.UserDomain.BuyProductCommand;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class PurchaseMapperTest extends UnitTest {
  @InjectMocks
  private PurchaseMapper purchaseMapper;

  @Nested
  class makeBuyProductCommand {
    @Test
    @DisplayName("상품구매 도메인으로 변환한다")
    void test1(){
      BuyProduct buyProduct = new BuyProduct();
      buyProduct.setUserId(12983712983L);
      buyProduct.setItems(List.of(1L, 2L));

      BuyProductCommand result = purchaseMapper.makeBuyProductCommand(buyProduct);
      assertThat(result).usingRecursiveComparison()
        .isEqualTo(buyProduct);
    }
  }
}