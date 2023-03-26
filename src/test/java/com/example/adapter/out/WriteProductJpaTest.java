package com.example.adapter.out;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.example.UnitTest;
import com.example.adapter.out.mapper.ProductOutMapper;
import com.example.domain.ProductDomain.ModifyProductCommand;
import com.example.domain.ProductDomain.ProductInfo;
import com.example.domain.ProductDomain.RegisterProductCommand;
import com.example.infra.database.Product;
import com.example.infra.database.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class WriteProductJpaTest extends UnitTest {
  @InjectMocks
  private WriteProductJpa writeProductJpa;
  @Mock
  private ProductRepository productRepository;
  @Mock
  private ProductOutMapper productOutMapper;

  @Nested
  class saveProductInfo {
    @Test
    @DisplayName("등록커맨드를 가지고 신규엔티티를 생성 후 DB에 저장한다")
    void test1(){
      Product newEntity = mock(Product.class);
      when(productOutMapper.makeProduct(any())).thenReturn(newEntity);

      Product savedEntity = mock(Product.class);
      when(productRepository.save(any())).thenReturn(savedEntity);

      ProductInfo productInfo = mock(ProductInfo.class);
      when(productOutMapper.toProductInfo(any())).thenReturn(productInfo);

      RegisterProductCommand command = mock(RegisterProductCommand.class);
      ProductInfo result = writeProductJpa.saveProduct(command);

      verify(productOutMapper).makeProduct(eq(command));
      verify(productRepository).save(eq(newEntity));
      verify(productOutMapper).toProductInfo(eq(savedEntity));

      assertThat(result).isEqualTo(productInfo);
    }
  }

  @Nested
  class removeProduct {
    @Test
    @DisplayName("요청된 아이디를 가지고 삭제 인터페이스를 호출한다")
    void test1(){
      long productId = 123123;

      writeProductJpa.removeProduct(productId);

      verify(productRepository).deleteById(eq(productId));
    }
  }

  @Nested
  class modifyProduct {
    @Test
    @DisplayName("수정커맨드를 가지고 엔티티를 찾은 후 엔티티의 수정메소드를 호출한다")
    void test1(){
      Product entity = mock(Product.class);
      when(productRepository.getReferenceById(anyLong())).thenReturn(entity);

      ProductInfo productInfo = mock(ProductInfo.class);
      when(productOutMapper.toProductInfo(any())).thenReturn(productInfo);

      ModifyProductCommand command = mock(ModifyProductCommand.class);
      ProductInfo result = writeProductJpa.modifyProduct(command);

      verify(productRepository).getReferenceById(eq(command.getProductId()));
      verify(entity).updateProduct(eq(command));
      verify(productOutMapper).toProductInfo(eq(entity));

      assertThat(result).isEqualTo(productInfo);
    }

    @Test
    @DisplayName("아이디를 찾지 못하면 예외를 던진다")
    void test2(){
      when(productRepository.getReferenceById(anyLong())).thenThrow(new RuntimeException());

      ModifyProductCommand command = mock(ModifyProductCommand.class);
      assertThrows(RuntimeException.class, () -> {
        writeProductJpa.modifyProduct(command);
      });

      verify(productRepository).getReferenceById(eq(command.getProductId()));
      verifyNoInteractions(productOutMapper);
    }
  }
}