package com.example.application;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.example.UnitTest;
import com.example.application.port.out.ReadProductStore;
import com.example.application.port.out.ReadPurchaseStore;
import com.example.application.port.out.ReadUserStore;
import com.example.application.port.out.WritePurchaseStore;
import com.example.common.Status4xxException;
import com.example.domain.ProductDomain.ProductInfo;
import com.example.domain.PurchaseDomain.PurchaseInfo;
import com.example.domain.PurchaseDomain.RegisterPurchaseCommand;
import com.example.domain.StaticDomain.ProductByYearAndMonth;
import com.example.domain.StaticDomain.UserByYearAndMonth;
import com.example.domain.UserDomain.BuyProductCommand;
import com.example.domain.UserDomain.UserInfo;
import com.example.infra.database.Product;
import com.example.infra.database.Purchase;
import com.example.infra.database.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class PurchaseServiceTest extends UnitTest {
  @InjectMocks
  private PurchaseService purchaseService;
  @Mock
  private WritePurchaseStore writePurchaseStore;
  @Mock
  private ReadProductStore readProductStore;
  @Mock
  private ReadPurchaseStore readPurchaseStore;
  @Mock
  private ReadUserStore readUserStore;

  @Nested
  class buyProduct {
    @Captor
    private ArgumentCaptor<List<RegisterPurchaseCommand>> captor;
    private final long userId = 12312127L;
    private final long productId = 192087123L;
    private final int price = 12312387;

    @Test
    @DisplayName("구매요청한 상품이 중복이 존재하면 예외를 발생시킨다")
    void test1(){
      BuyProductCommand command = new BuyProductCommand(userId, List.of(productId, productId));
      assertThrows(Status4xxException.class, () -> {
        purchaseService.buyProduct(command);
      });

      verifyNoInteractions(readUserStore);
      verifyNoInteractions(readProductStore);
      verifyNoInteractions(writePurchaseStore);
    }

    @Test
    @DisplayName("사용자 정보가 존재하지 않으면 예외를 발생시킨다")
    void test0(){
      when(readUserStore.findUserByUserId(anyLong())).thenReturn(Optional.empty());

      BuyProductCommand command = new BuyProductCommand(userId, List.of(productId));
      assertThrows(Status4xxException.class, () -> {
        purchaseService.buyProduct(command);
      });

      verify(readUserStore).findUserByUserId(eq(command.getUserId()));
      verifyNoInteractions(readProductStore);
      verifyNoInteractions(writePurchaseStore);
    }

    @Test
    @DisplayName("구매요청한 상품이 존재하지 않으면 예외를 발생시킨다")
    void test2() {
      when(readUserStore.findUserByUserId(anyLong())).thenReturn(Optional.of(mock(UserInfo.class)));

      ProductInfo productInfo = mock(ProductInfo.class);
      when(readProductStore.getProductsByProductIds(anyList())).thenReturn(List.of(productInfo));
      when(productInfo.getProductId()).thenReturn(12398172937123L);

      BuyProductCommand command = new BuyProductCommand(
        userId,
        List.of(productId)
      );
      assertThrows(
        Status4xxException.class,
        () -> {
          purchaseService.buyProduct(command);
        }
      );

      verify(readUserStore).findUserByUserId(eq(command.getUserId()));
      verify(readProductStore).getProductsByProductIds(eq(command.getItems()));
      verifyNoInteractions(writePurchaseStore);
    }

    @Test
    @DisplayName("사용자 구매요청정보를 구매커맨드 도메인으로 변환후 구매등록 인터페이스를 호출한다")
    void test3(){
      when(readUserStore.findUserByUserId(anyLong())).thenReturn(Optional.of(mock(UserInfo.class)));

      ProductInfo productInfo = mock(ProductInfo.class);
      when(readProductStore.getProductsByProductIds(anyList())).thenReturn(List.of(productInfo));
      when(productInfo.getProductId()).thenReturn(productId);
      when(productInfo.getPrice()).thenReturn(price);

      PurchaseInfo purchaseInfo = mock(PurchaseInfo.class);
      when(writePurchaseStore.savePurchases(any())).thenReturn(List.of(purchaseInfo));

      BuyProductCommand command = new BuyProductCommand(userId, List.of(productId));
      List<PurchaseInfo> results = purchaseService.buyProduct(command);

      verify(readUserStore).findUserByUserId(eq(command.getUserId()));
      verify(readProductStore).getProductsByProductIds(eq(command.getItems()));
      verify(writePurchaseStore).savePurchases(captor.capture());

      List<RegisterPurchaseCommand> purchaseCommands = captor.getValue();

      assertThat(results.size()).isOne();
      assertThat(purchaseCommands.size()).isOne();
      assertThat(purchaseCommands.get(0).getUserId()).isEqualTo(userId);
      assertThat(purchaseCommands.get(0).getProductId()).isEqualTo(productId);
      assertThat(purchaseCommands.get(0).getPrice()).isEqualTo(price);
    }
  }

  @Nested
  class Sales {
    private final User user1 = User.builder().userId(1L).userName("일길동").build();
    private final User user2 = User.builder().userId(2L).userName("이길동").build();
    private final User user3 = User.builder().userId(3L).userName("삼길동").build();
    private final Product product1 = Product.builder().productId(1L).productName("일삼품").build();
    private final Product product2 = Product.builder().productId(2L).productName("이삼품").build();
    private final Product product3 = Product.builder().productId(3L).productName("삼삼품").build();
    private final int REQUEST_YEAR = 2020;

    private final PurchaseInfo p11 = new PurchaseInfo(Purchase.builder()
      .price(1000).yearOfCreatedAt(REQUEST_YEAR).monthOfCreatedAt(1)
      .user(user1)
      .product(product1)
      .build());
    private final PurchaseInfo p12 = new PurchaseInfo(Purchase.builder()
      .price(2000).yearOfCreatedAt(REQUEST_YEAR).monthOfCreatedAt(1)
      .user(user1)
      .product(product2)
      .build());
    private final PurchaseInfo p13 = new PurchaseInfo(Purchase.builder()
      .price(3000).yearOfCreatedAt(REQUEST_YEAR).monthOfCreatedAt(2)
      .user(user1)
      .product(product3)
      .build());
    private final PurchaseInfo p14 = new PurchaseInfo(Purchase.builder()
      .price(1000).yearOfCreatedAt(REQUEST_YEAR).monthOfCreatedAt(3)
      .user(user1)
      .product(product1)
      .build());
    private final PurchaseInfo p21 = new PurchaseInfo( Purchase.builder()
      .price(1000).yearOfCreatedAt(REQUEST_YEAR).monthOfCreatedAt(2)
      .user(user2)
      .product(product1)
      .build());
    private final PurchaseInfo p22 = new PurchaseInfo(Purchase.builder()
      .price(2000).yearOfCreatedAt(REQUEST_YEAR).monthOfCreatedAt(2)
      .user(user2)
      .product(product2)
      .build());
    private final PurchaseInfo p23 = new PurchaseInfo(Purchase.builder()
      .price(3000).yearOfCreatedAt(REQUEST_YEAR).monthOfCreatedAt(3)
      .user(user2)
      .product(product3)
      .build());

    private final PurchaseInfo p31 = new PurchaseInfo(Purchase.builder()
      .price(3000).yearOfCreatedAt(REQUEST_YEAR).monthOfCreatedAt(1)
      .user(user3)
      .product(product3)
      .build());
    @Nested
    class getSalesRecordsByUser {
      private void priceTest(List<UserByYearAndMonth> results,
        long userId, int month, long expectedAmount){
        long totalAmount = results.stream()
          .filter(i -> i.getUserId().equals(userId))
          .filter(i -> i.getYear() == REQUEST_YEAR)
          .filter(i -> i.getMonth() == month)
          .findFirst()
          .get()
          .getTotalAmount();

        assertThat(totalAmount).isEqualTo(expectedAmount);
      }

      @Test
      @DisplayName("사용자의 년월 별 총합계 금액을 계산하고 해당 결과값을 리스트로 반환한다")
      void test1(){
        when(readPurchaseStore.getPurchasesByYear(anyInt())).thenReturn(List.of(p11, p12, p13, p14, p21, p22, p23, p31));

        List<UserByYearAndMonth> results = purchaseService.getSalesRecordsByUser(REQUEST_YEAR);

        verify(readPurchaseStore).getPurchasesByYear(eq(REQUEST_YEAR));

        Set<Long> userIds = results.stream().map(UserByYearAndMonth::getUserId)
          .collect(Collectors.toSet());
        assertThat(userIds).contains(user1.getUserId(),user2.getUserId(),user3.getUserId());

        Set<String> user1Result = results.stream()
          .filter(i -> i.getUserId().equals(user1.getUserId()))
          .map(i -> i.getYear() + String.format("%02d", i.getMonth()))
          .collect(Collectors.toSet());
        assertThat(user1Result).contains("202001","202002","202003");
        this.priceTest(results, user1.getUserId(), 1, 3000);
        this.priceTest(results, user1.getUserId(), 2, 3000);
        this.priceTest(results, user1.getUserId(), 3, 1000);

        Set<String> user2Result = results.stream()
          .filter(i -> i.getUserId().equals(user2.getUserId()))
          .map(i -> i.getYear() + String.format("%02d", i.getMonth()))
          .collect(Collectors.toSet());
        assertThat(user2Result).contains("202002","202003");
        this.priceTest(results, user2.getUserId(), 2, 3000);
        this.priceTest(results, user2.getUserId(), 3, 3000);

        Set<String> user3Result = results.stream()
          .filter(i -> i.getUserId().equals(user3.getUserId()))
          .map(i -> i.getYear() + String.format("%02d", i.getMonth()))
          .collect(Collectors.toSet());
        assertThat(user3Result).contains("202001");
        this.priceTest(results, user3.getUserId(), 1, 3000);
      }
    }

    @Nested
    class getSalesRecordsByProduct {
      private void priceAndUserCountTest(List<ProductByYearAndMonth> results,
        long productId, int month, long expectedAmount, int expectedUserCount){
        long totalAmount = results.stream()
          .filter(i -> i.getProductId().equals(productId))
          .filter(i -> i.getYear() == REQUEST_YEAR)
          .filter(i -> i.getMonth() == month)
          .findFirst()
          .get()
          .getTotalAmount();
        int userCount = results.stream()
          .filter(i -> i.getProductId().equals(productId))
          .filter(i -> i.getYear() == REQUEST_YEAR)
          .filter(i -> i.getMonth() == month)
          .findFirst()
          .get()
          .getTotalUserCount();

        assertThat(totalAmount).isEqualTo(expectedAmount);
        assertThat(userCount).isEqualTo(expectedUserCount);
      }

      @Test
      @DisplayName("상품의 년월도 별 총금액 및 구매사용자 수를 반환한다")
      void test1(){
        when(readPurchaseStore.getPurchasesByYear(anyInt())).thenReturn(List.of(p11, p12, p13, p14, p21, p22, p23, p31));

        List<ProductByYearAndMonth> results = purchaseService.getSalesRecordsByProduct(REQUEST_YEAR);

        verify(readPurchaseStore).getPurchasesByYear(eq(REQUEST_YEAR));

        Set<Long> productIds = results.stream().map(ProductByYearAndMonth::getProductId)
          .collect(Collectors.toSet());
        assertThat(productIds).contains(product1.getProductId(), product2.getProductId(), product3.getProductId());

        Set<String> product1Result = results.stream()
          .filter(i -> i.getProductId().equals(product1.getProductId()))
          .map(i -> i.getYear() + String.format("%02d", i.getMonth()))
          .collect(Collectors.toSet());
        assertThat(product1Result).contains("202001","202002","202003");
        this.priceAndUserCountTest(results, product1.getProductId(), 1, 1000,1);
        this.priceAndUserCountTest(results, product1.getProductId(), 2, 1000,1);
        this.priceAndUserCountTest(results, product1.getProductId(), 3, 1000,1);

        Set<String> product2Result = results.stream()
          .filter(i -> i.getProductId().equals(product2.getProductId()))
          .map(i -> i.getYear() + String.format("%02d", i.getMonth()))
          .collect(Collectors.toSet());
        assertThat(product2Result).contains("202001","202002");
        this.priceAndUserCountTest(results, product2.getProductId(), 1, 2000,1);
        this.priceAndUserCountTest(results, product2.getProductId(), 2, 2000,1);

        Set<String> product3Result = results.stream()
          .filter(i -> i.getProductId().equals(product3.getProductId()))
          .map(i -> i.getYear() + String.format("%02d", i.getMonth()))
          .collect(Collectors.toSet());
        assertThat(product3Result).contains("202001","202002","202003");
        this.priceAndUserCountTest(results, product3.getProductId(), 1, 3000,1);
        this.priceAndUserCountTest(results, product3.getProductId(), 2, 3000,1);
        this.priceAndUserCountTest(results, product3.getProductId(), 3, 3000,1);
      }
    }
  }
}