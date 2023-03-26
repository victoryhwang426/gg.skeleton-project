package com.example.adapter.out;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.UnitTest;
import com.example.adapter.out.mapper.ProductOutMapper;
import com.example.domain.ProductDomain.ProductInfo;
import com.example.infra.database.Product;
import com.example.infra.database.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class ReadProductJpaTest extends UnitTest {
  @InjectMocks
  private ReadProductJpa readProductJpa;
  @Mock
  private ProductRepository productRepository;
  @Mock
  private ProductOutMapper productOutMapper;

  @Nested
  class findProductInfoByProductId {
    @Test
    @DisplayName("상품의 아이디를 이용하여 상품정보를 찾는다")
    void test1(){
      Product product = mock(Product.class);
      when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

      ProductInfo domain = mock(ProductInfo.class);
      when(productOutMapper.toProductInfo(any())).thenReturn(domain);

      Optional<ProductInfo> result =
        readProductJpa.findProductInfoByProductId(1);

      verify(productRepository).findById(eq(1L));
      verify(productOutMapper).toProductInfo(eq(product));

      assertThat(result).isPresent();
      assertThat(result.get()).isEqualTo(domain);
    }
  }

  @Nested
  class getAllProducts {
    @Test
    @DisplayName("등록된 모든 상품정보를 찾는다")
    void test1(){
      Product product = mock(Product.class);
      when(productRepository.findAll()).thenReturn(List.of(product));

      ProductInfo domain = mock(ProductInfo.class);
      when(productOutMapper.toProductInfo(any())).thenReturn(domain);

      List<ProductInfo> results =
        readProductJpa.getAllProducts();

      verify(productRepository).findAll();
      verify(productOutMapper, times(1)).toProductInfo(eq(product));

      assertThat(results.size()).isOne();
      assertThat(results).contains(domain);
    }
  }

  @Nested
  class getProductsByProductIds {
    @Test
    @DisplayName("요청된 아이디를 가지고 상품정보를 찾는다")
    void test1(){
      Product product = mock(Product.class);
      when(productRepository.findAllById(any())).thenReturn(List.of(product));

      ProductInfo domain = mock(ProductInfo.class);
      when(productOutMapper.toProductInfo(any())).thenReturn(domain);

      List<Long> productIds = List.of(1L);
      List<ProductInfo> results =
        readProductJpa.getProductsByProductIds(productIds);

      verify(productRepository).findAllById(eq(productIds));
      verify(productOutMapper, times(1)).toProductInfo(eq(product));

      assertThat(results.size()).isOne();
      assertThat(results).contains(domain);
    }
  }
}