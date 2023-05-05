package com.example.adapter.in.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.UnitTest;
import com.example.adapter.in.dto.ProductDto.Modify;
import com.example.adapter.in.dto.ProductDto.Register;
import com.example.domain.ProductDomain.ModifyProductCommand;
import com.example.domain.ProductDomain.RegisterProductCommand;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class ProductMapperTest extends UnitTest {

  @InjectMocks
  private ProductMapper productMapper;

  @Nested
  class makeRegisterProductCommand {

    @Test
    @DisplayName("convert registering new product command class")
    void test1() {
      Register register = new Register();
      register.setProductName(RandomString.make());
      register.setPrice(1239812);

      RegisterProductCommand result = productMapper.makeRegisterProductCommand(register);
      assertThat(result).usingRecursiveComparison()
          .isEqualTo(register);
    }
  }

  @Nested
  class makeModifyProductCommand {

    @Test
    @DisplayName("convert modifying the product command class")
    void test1() {
      Modify modify = new Modify();
      modify.setProductName(RandomString.make());
      modify.setPrice(123123151);

      ModifyProductCommand result = productMapper.makeModifyProductCommand(12312318L, modify);
      assertThat(result.getProductId()).isEqualTo(12312318L);
      assertThat(result).usingRecursiveComparison()
          .ignoringFields("productId")
          .isEqualTo(modify);
    }
  }
}