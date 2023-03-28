package com.example.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.example.UnitTest;
import com.example.application.port.out.ReadProductStore;
import com.example.application.port.out.WriteProductStore;
import com.example.common.Status4xxException;
import com.example.domain.ProductDomain.ModifyProductCommand;
import com.example.domain.ProductDomain.ProductInfo;
import com.example.domain.ProductDomain.RegisterProductCommand;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class ProductServiceTest extends UnitTest {
  @InjectMocks
  private ProductService productService;
  @Mock
  private WriteProductStore writeProductStore;
  @Mock
  private ReadProductStore readProductStore;

  @Nested
  class getAllProducts {
    @Test
    @DisplayName("등록된 모든 상품을 획득하는 인터페이스를 호출한다")
    void test1(){
      when(readProductStore.getAllProducts()).thenReturn(Collections.emptyList());

      List<ProductInfo> results = productService.getAllProducts();

      verify(readProductStore).getAllProducts();

      assertThat(results.isEmpty()).isTrue();
    }
  }

  @Nested
  class getProductBy {
    @Test
    @DisplayName("상품 아이디로 상품을 찾는 인터페이스를 호출한다")
    void test1(){
      ProductInfo productInfo = mock(ProductInfo.class);
      when(readProductStore.findProductInfoByProductId(anyLong())).thenReturn(Optional.of(productInfo));

      long productId = 123098123;
      ProductInfo result = productService.getProductBy(productId);

      verify(readProductStore).findProductInfoByProductId(eq(productId));

      assertThat(result).isEqualTo(productInfo);
    }

    @Test
    @DisplayName("상품정보가 존재하지 않으면 예외를 발생시킨다")
    void test2(){
      when(readProductStore.findProductInfoByProductId(anyLong())).thenReturn(Optional.empty());

      long productId = 123098123;
      assertThrows(Status4xxException.class, () -> {
        productService.getProductBy(productId);
      });

      verify(readProductStore).findProductInfoByProductId(eq(productId));
    }
  }

  @Nested
  class registerProduct {
    @Test
    @DisplayName("등록커맨드를 이용하여 상품등록 인터페이스를 호출한다")
    void test1(){
      ProductInfo productInfo = mock(ProductInfo.class);
      when(writeProductStore.saveProduct(any())).thenReturn(productInfo);

      RegisterProductCommand command = mock(RegisterProductCommand.class);
      ProductInfo result = productService.registerProduct(command);

      verify(writeProductStore).saveProduct(eq(command));

      assertThat(result).isEqualTo(productInfo);
    }
  }

  @Nested
  class modifyProduct {
    @Test
    @DisplayName("수정커맨드를 이용하여 상품수정 인터페이스를 호출한다")
    void test1(){
      ProductInfo productInfo = mock(ProductInfo.class);
      when(readProductStore.findProductInfoByProductId(anyLong())).thenReturn(Optional.of(productInfo));

      ProductInfo afterProductInfo = mock(ProductInfo.class);
      when(writeProductStore.modifyProduct(any())).thenReturn(afterProductInfo);

      ModifyProductCommand command = mock(ModifyProductCommand.class);
      ProductInfo result = productService.modifyProduct(command);

      verify(readProductStore).findProductInfoByProductId(eq(command.getProductId()));
      verify(writeProductStore).modifyProduct(eq(command));

      assertThat(result).isEqualTo(afterProductInfo);
    }

    @Test
    @DisplayName("상품아이디로 상품을 찾을 수 없으면 예외를 던진다")
    void test2(){
      when(readProductStore.findProductInfoByProductId(anyLong())).thenReturn(Optional.empty());

      ModifyProductCommand command = mock(ModifyProductCommand.class);
      assertThrows(Status4xxException.class, () -> {
        productService.modifyProduct(command);
      });

      verify(readProductStore).findProductInfoByProductId(eq(command.getProductId()));
      verifyNoInteractions(writeProductStore);
    }
  }

  @Nested
  class deleteProduct {
    @Test
    @DisplayName("상품아이디를 이용하여 삭제 인터페이스를 호출한다")
    void test1(){
      ProductInfo productInfo = mock(ProductInfo.class);
      when(readProductStore.findProductInfoByProductId(anyLong())).thenReturn(Optional.of(productInfo));
      doNothing().when(writeProductStore).removeProduct(anyLong());

      long productId = 123123;
      productService.deleteProduct(productId);

      verify(readProductStore).findProductInfoByProductId(eq(productId));
      verify(writeProductStore).removeProduct(eq(productId));
    }

    @Test
    @DisplayName("상품아이디로 상품을 찾을 수 없으면 예외를 던진다")
    void test2(){
      when(readProductStore.findProductInfoByProductId(anyLong())).thenReturn(Optional.empty());

      long productId = 123123;
      assertThrows(Status4xxException.class, () -> {
        productService.deleteProduct(productId);
      });

      verify(readProductStore).findProductInfoByProductId(eq(productId));
      verifyNoInteractions(writeProductStore);
    }
  }
}