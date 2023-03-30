package com.example.adapter.in;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.UnitTest;
import com.example.adapter.in.dto.ProductDto.Register;
import com.example.adapter.in.dto.PurchaseDto;
import com.example.adapter.in.mapper.PurchaseMapper;
import com.example.application.port.in.PurchaseUseCases;
import com.example.common.ApiResponse.ProcessStatus;
import com.example.domain.PurchaseDomain.PurchaseInfo;
import com.example.domain.StaticDomain.ProductByYearAndMonth;
import com.example.domain.StaticDomain.UserByYearAndMonth;
import com.example.domain.UserDomain.BuyProductCommand;
import com.example.handler.RestControllerExceptionAdvisor;
import com.example.infra.database.Product;
import com.example.infra.database.Purchase;
import com.example.infra.database.User;
import java.util.List;
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

class PurchaseControllerTest extends UnitTest {
  @InjectMocks
  private PurchaseController purchaseController;
  @Mock
  private PurchaseUseCases purchaseUseCases;
  @Mock
  private PurchaseMapper purchaseMapper;
  private final String CONTEXT = "/purchases";

  @BeforeEach
  void init() {
    mockMvc = MockMvcBuilders.standaloneSetup(purchaseController)
      .setControllerAdvice(RestControllerExceptionAdvisor.class)
      .build();
  }

  @Nested
  class RestControllerAdviceTest {
    @Test
    @DisplayName("Min 테스트")
    void test1() throws Exception {
      PurchaseDto.BuyProduct buyProduct = new PurchaseDto.BuyProduct();
      buyProduct.setItems(List.of(1L));
      mockMvc.perform(post(CONTEXT)
          .content(objectMapper.writeValueAsString(buyProduct))
          .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code", is(400), Integer.class))
        .andExpect(jsonPath("$.message", is("사용자번호는 0보다 커야합니다.")))
        .andDo(print());
    }

    @Test
    @DisplayName("NotEmpty 테스트")
    void test2() throws Exception {
      PurchaseDto.BuyProduct buyProduct = new PurchaseDto.BuyProduct();
      buyProduct.setUserId(1);
      mockMvc.perform(post(CONTEXT)
          .content(objectMapper.writeValueAsString(buyProduct))
          .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code", is(400), Integer.class))
        .andExpect(jsonPath("$.message", is("구매할 상품아이디가 존재하지 않습니다.")))
        .andDo(print());
    }
  }

  @Nested
  class buyProduct {
    @Captor
    private ArgumentCaptor<PurchaseDto.BuyProduct> captor;

    @Test
    @DisplayName("사용자가 상품을 구매할 수 있는 인터페이스를 호출한다")
    void test1() throws Exception {
      BuyProductCommand command = mock(BuyProductCommand.class);
      when(purchaseMapper.makeBuyProductCommand(any())).thenReturn(command);

      PurchaseInfo purchaseInfo = new PurchaseInfo(Purchase.builder()
        .purchaseId(123123L)
        .purchaseNumber(12083712L)
        .user(User.builder()
          .userId(1231623L)
          .userName("asefoiujaefjp")
          .build())
        .product(Product.builder()
          .productId(12397162L)
          .productName("123087129376cgjk")
          .build())
        .price(10000)
        .yearOfCreatedAt(2020)
        .monthOfCreatedAt(1)
        .build());
      when(purchaseUseCases.buyProduct(any())).thenReturn(List.of(purchaseInfo));

      PurchaseDto.BuyProduct buyProduct = new PurchaseDto.BuyProduct();
      buyProduct.setUserId(1L);
      buyProduct.setItems(List.of(1L));
      mockMvc.perform(post(
          CONTEXT)
          .content(objectMapper.writeValueAsString(buyProduct))
          .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.processStatus", is(ProcessStatus.FINISHED.toString())))
        .andExpect(jsonPath("$.result.[0].purchaseId", is(purchaseInfo.getPurchaseId()), Long.class))
        .andExpect(jsonPath("$.result.[0].purchaseNumber", is(purchaseInfo.getPurchaseNumber()), Long.class))
        .andExpect(jsonPath("$.result.[0].userId", is(purchaseInfo.getUserId()), Long.class))
        .andExpect(jsonPath("$.result.[0].userName", is(purchaseInfo.getUserName()), String.class))
        .andExpect(jsonPath("$.result.[0].productId", is(purchaseInfo.getProductId()), Long.class))
        .andExpect(jsonPath("$.result.[0].productName", is(purchaseInfo.getProductName()), String.class))
        .andExpect(jsonPath("$.result.[0].price", is(purchaseInfo.getPrice()), Integer.class))
        .andExpect(jsonPath("$.result.[0].yearOfCreatedAt", is(purchaseInfo.getYearOfCreatedAt()), Integer.class))
        .andExpect(jsonPath("$.result.[0].monthOfCreatedAt", is(purchaseInfo.getMonthOfCreatedAt()), Integer.class))
        .andDo(print());

      verify(purchaseMapper).makeBuyProductCommand(captor.capture());
      verify(purchaseUseCases).buyProduct(eq(command));

      PurchaseDto.BuyProduct result = captor.getValue();
      assertThat(result).usingRecursiveComparison()
        .isEqualTo(buyProduct);
    }
  }

  @Nested
  class getUserStatics {
    @Test
    @DisplayName("사용자의 년월별 집계정보를 획득한다")
    void test1() throws Exception {
      UserByYearAndMonth userByYearAndMonth =
        new UserByYearAndMonth(1L, "사용자 1번", 2020, 1, 1000);
      when(purchaseUseCases.getSalesRecordsByUser(anyInt())).thenReturn(List.of(userByYearAndMonth));

      int year = 2020;
      mockMvc.perform(get(
          CONTEXT + "/{year}/user", year)
          .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.processStatus", is(ProcessStatus.FINISHED.toString())))
        .andExpect(jsonPath("$.result.[0].userId", is(userByYearAndMonth.getUserId()), Long.class))
        .andExpect(jsonPath("$.result.[0].userName", is(userByYearAndMonth.getUserName()), String.class))
        .andExpect(jsonPath("$.result.[0].year", is(userByYearAndMonth.getYear()), Integer.class))
        .andExpect(jsonPath("$.result.[0].month", is(userByYearAndMonth.getMonth()), Integer.class))
        .andExpect(jsonPath("$.result.[0].totalAmount", is(userByYearAndMonth.getTotalAmount()), Long.class))
        .andDo(print());

      verify(purchaseUseCases).getSalesRecordsByUser(eq(year));
    }
  }

  @Nested
  class getProductStatics {
    @Test
    @DisplayName("상품의 년월별 집계정보를 획득한다")
    void test1() throws Exception {
      ProductByYearAndMonth productByYearAndMonth =
        new ProductByYearAndMonth(1L, "상품 1번", 2020, 1, 1000, 1);
      when(purchaseUseCases.getSalesRecordsByProduct(anyInt())).thenReturn(List.of(productByYearAndMonth));

      int year = 2020;
      mockMvc.perform(get(
          CONTEXT + "/{year}/product", year)
          .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.processStatus", is(ProcessStatus.FINISHED.toString())))
        .andExpect(jsonPath("$.result.[0].productId", is(productByYearAndMonth.getProductId()), Long.class))
        .andExpect(jsonPath("$.result.[0].productName", is(productByYearAndMonth.getProductName())))
        .andExpect(jsonPath("$.result.[0].year", is(productByYearAndMonth.getYear()), Integer.class))
        .andExpect(jsonPath("$.result.[0].month", is(productByYearAndMonth.getMonth()), Integer.class))
        .andExpect(jsonPath("$.result.[0].totalAmount", is(productByYearAndMonth.getTotalAmount()), Long.class))
        .andExpect(jsonPath("$.result.[0].totalUserCount", is(productByYearAndMonth.getTotalUserCount()), Integer.class))
        .andDo(print());

      verify(purchaseUseCases).getSalesRecordsByProduct(eq(year));
    }
  }
}