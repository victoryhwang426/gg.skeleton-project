package com.example.adapter.in;

import static com.example.common.ApiResponse.ProcessStatus.STOPPED_BY_VALIDATION;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.UnitTest;
import com.example.adapter.in.dto.ProductDto.Modify;
import com.example.adapter.in.dto.ProductDto.Register;
import com.example.adapter.in.mapper.ProductMapper;
import com.example.application.port.in.ProductUseCases;
import com.example.common.ApiResponse.ProcessStatus;
import com.example.domain.ProductDomain.ModifyProductCommand;
import com.example.domain.ProductDomain.ProductInfo;
import com.example.domain.ProductDomain.RegisterProductCommand;
import com.example.handler.RestControllerExceptionAdvisor;
import com.example.infra.database.Product;
import java.util.List;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class ProductControllerTest extends UnitTest {

  @InjectMocks
  private ProductController productController;
  @Mock
  private ProductUseCases productUseCases;
  @Mock
  private ProductMapper productMapper;
  private final String CONTEXT = "/products";

  @BeforeEach
  void init() {
    mockMvc = MockMvcBuilders.standaloneSetup(productController)
        .setControllerAdvice(RestControllerExceptionAdvisor.class)
        .build();
  }

  @Nested
  class RestControllerAdviceTest {

    @Test
    @DisplayName("NotBlank Test")
    void test1() throws Exception {
      Register register = new Register();
      register.setPrice(102873012);
      mockMvc.perform(post(CONTEXT)
              .content(objectMapper.writeValueAsString(register))
              .contentType(MediaType.APPLICATION_JSON)
          )
          .andExpect(status().is4xxClientError())
          .andExpect(jsonPath("$.errorMessage").isNotEmpty())
          .andExpect(jsonPath("$.processStatus", is(STOPPED_BY_VALIDATION.toString())))
          .andDo(print());
    }

    @Test
    @DisplayName("NotNull Test")
    void test2() throws Exception {
      Register register = new Register();
      register.setProductName("aopeijfaoisej");
      mockMvc.perform(post(CONTEXT)
              .content(objectMapper.writeValueAsString(register))
              .contentType(MediaType.APPLICATION_JSON)
          )
          .andExpect(status().is4xxClientError())
          .andExpect(jsonPath("$.errorMessage").isNotEmpty())
          .andExpect(jsonPath("$.processStatus", is(STOPPED_BY_VALIDATION.toString())))
          .andDo(print());
    }
  }

  @Nested
  class registerProduct {

    @Captor
    private ArgumentCaptor<Register> captor;

    @Test
    @DisplayName("call the interface to register new product")
    void test1() throws Exception {
      RegisterProductCommand command = mock(RegisterProductCommand.class);
      when(productMapper.makeRegisterProductCommand(any())).thenReturn(command);

      long productId = 1;
      String productName = RandomString.make();
      int price = 20000;
      ProductInfo productInfo = new ProductInfo(Product.builder()
          .productId(productId)
          .productName(productName)
          .price(price)
          .build());
      when(productUseCases.registerProduct(any())).thenReturn(productInfo);

      Register register = new Register();
      register.setProductName(productName);
      register.setPrice(price);
      mockMvc.perform(post(CONTEXT)
              .content(objectMapper.writeValueAsString(register))
              .contentType(MediaType.APPLICATION_JSON)
          )
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.processStatus", is(ProcessStatus.FINISHED.toString())))
          .andExpect(jsonPath("$.result.productId", is(productInfo.getProductId()), Long.class))
          .andExpect(
              jsonPath("$.result.productName", is(productInfo.getProductName()), String.class))
          .andExpect(jsonPath("$.result.price", is(productInfo.getPrice()), Integer.class))
          .andDo(print());

      verify(productMapper).makeRegisterProductCommand(captor.capture());
      verify(productUseCases).registerProduct(eq(command));

      Register result = captor.getValue();
      assertThat(result).usingRecursiveComparison()
          .isEqualTo(register);
    }
  }

  @Nested
  class getProducts {

    @Test
    @DisplayName("call the interface to get all registered products")
    void test1() throws Exception {
      long productId = 123987;
      String productName = RandomString.make();
      int price = 20000;
      ProductInfo productInfo = new ProductInfo(Product.builder()
          .productId(productId)
          .productName(productName)
          .price(price)
          .build());
      when(productUseCases.getAllProducts()).thenReturn(List.of(productInfo));

      mockMvc.perform(get(CONTEXT)
              .contentType(MediaType.APPLICATION_JSON)
          )
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.processStatus", is(ProcessStatus.FINISHED.toString())))
          .andExpect(jsonPath("$.result.[0].productId", is(productInfo.getProductId()), Long.class))
          .andExpect(
              jsonPath("$.result.[0].productName", is(productInfo.getProductName()), String.class))
          .andExpect(jsonPath("$.result.[0].price", is(productInfo.getPrice()), Integer.class))
          .andDo(print());

      verify(productUseCases).getAllProducts();
    }
  }

  @Nested
  class getProduct {

    @Test
    @DisplayName("call the interface to get product")
    void test1() throws Exception {
      long productId = 123987;
      String productName = RandomString.make();
      int price = 20000;
      ProductInfo productInfo = new ProductInfo(Product.builder()
          .productId(productId)
          .productName(productName)
          .price(price)
          .build());
      when(productUseCases.getProductBy(anyLong())).thenReturn(productInfo);

      mockMvc.perform(get(
              CONTEXT + "/{productId}", productId)
              .contentType(MediaType.APPLICATION_JSON)
          )
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.processStatus", is(ProcessStatus.FINISHED.toString())))
          .andExpect(jsonPath("$.result.productId", is(productInfo.getProductId()), Long.class))
          .andExpect(
              jsonPath("$.result.productName", is(productInfo.getProductName()), String.class))
          .andExpect(jsonPath("$.result.price", is(productInfo.getPrice()), Integer.class))
          .andDo(print());

      verify(productUseCases).getProductBy(eq(productId));
    }
  }

  @Nested
  class modifyProduct {

    @Captor
    private ArgumentCaptor<Modify> captor;

    @Test
    @DisplayName("call the interface to modify the product")
    void test1() throws Exception {
      ModifyProductCommand command = mock(ModifyProductCommand.class);
      when(productMapper.makeModifyProductCommand(anyLong(), any())).thenReturn(command);

      long productId = 123987;
      String productName = RandomString.make();
      int price = 20000;
      ProductInfo productInfo = new ProductInfo(Product.builder()
          .productId(productId)
          .productName(productName)
          .price(price)
          .build());
      when(productUseCases.modifyProduct(any())).thenReturn(productInfo);

      Modify modify = new Modify();
      modify.setProductName(productName);
      modify.setPrice(price);
      mockMvc.perform(patch(
              CONTEXT + "/{productId}/modify", productId)
              .content(objectMapper.writeValueAsString(modify))
              .contentType(MediaType.APPLICATION_JSON)
          )
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.processStatus", is(ProcessStatus.FINISHED.toString())))
          .andExpect(jsonPath("$.result.productId", is(productInfo.getProductId()), Long.class))
          .andExpect(
              jsonPath("$.result.productName", is(productInfo.getProductName()), String.class))
          .andExpect(jsonPath("$.result.price", is(productInfo.getPrice()), Integer.class))
          .andDo(print());

      verify(productMapper).makeModifyProductCommand(eq(productId), captor.capture());
      verify(productUseCases).modifyProduct(eq(command));

      Modify result = captor.getValue();
      assertThat(result).usingRecursiveComparison()
          .isEqualTo(modify);
    }
  }

  @Nested
  class deleteProduct {

    @Test
    @DisplayName("call the interface to delete the product")
    void test1() throws Exception {
      doNothing().when(productUseCases).deleteProduct(anyLong());

      long productId = 123987;
      mockMvc.perform(patch(
              CONTEXT + "/{productId}/delete", productId)
              .contentType(MediaType.APPLICATION_JSON)
          )
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.processStatus", is(ProcessStatus.FINISHED.toString())))
          .andDo(print());

      verify(productUseCases).deleteProduct(eq(productId));
    }
  }
}