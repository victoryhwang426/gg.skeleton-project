package com.example.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @DisplayName("Call the interface to get all registered products")
    void test1() {
      when(readProductStore.getAllProducts()).thenReturn(Collections.emptyList());

      List<ProductInfo> results = productService.getAllProducts();

      verify(readProductStore).getAllProducts();

      assertThat(results.isEmpty()).isTrue();
    }
  }

  @Nested
  class getProductBy {

    @Test
    @DisplayName("Call the interface to find a product by product ID")
    void test1() {
      ProductInfo productInfo = mock(ProductInfo.class);
      when(readProductStore.findProductInfoByProductId(anyLong())).thenReturn(
          Optional.of(productInfo));

      long productId = 123098123;
      ProductInfo result = productService.getProductBy(productId);

      verify(readProductStore).findProductInfoByProductId(eq(productId));

      assertThat(result).isEqualTo(productInfo);
    }

    @Test
    @DisplayName("throw exception if product is not found by id")
    void test2() {
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
    @DisplayName("call the interface to register product")
    void test1() {
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
    @DisplayName("call the interface to modify product")
    void test1() {
      ProductInfo productInfo = mock(ProductInfo.class);
      when(readProductStore.findProductInfoByProductId(anyLong())).thenReturn(
          Optional.of(productInfo));

      ProductInfo afterProductInfo = mock(ProductInfo.class);
      when(writeProductStore.modifyProduct(any())).thenReturn(afterProductInfo);

      ModifyProductCommand command = mock(ModifyProductCommand.class);
      ProductInfo result = productService.modifyProduct(command);

      verify(readProductStore).findProductInfoByProductId(eq(command.getProductId()));
      verify(writeProductStore).modifyProduct(eq(command));

      assertThat(result).isEqualTo(afterProductInfo);
    }

    @Test
    @DisplayName("throw exception if product is not found by id")
    void test2() {
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
    @DisplayName("call the interface to delete product")
    void test1() {
      ProductInfo productInfo = mock(ProductInfo.class);
      when(readProductStore.findProductInfoByProductId(anyLong())).thenReturn(
          Optional.of(productInfo));
      doNothing().when(writeProductStore).removeProduct(anyLong());

      long productId = 123123;
      productService.deleteProduct(productId);

      verify(readProductStore).findProductInfoByProductId(eq(productId));
      verify(writeProductStore).removeProduct(eq(productId));
    }

    @Test
    @DisplayName("throw exception if product is not found by id")
    void test2() {
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