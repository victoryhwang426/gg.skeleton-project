package com.example.application.port;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.UnitTest;
import com.example.application.port.out.ReadPurchaseStore;
import com.example.domain.PurchaseDomain.PurchaseInfo;
import com.example.domain.StaticDomain.ProductByYearAndMonth;
import com.example.domain.StaticDomain.UserByYearAndMonth;
import com.example.infra.database.Product;
import com.example.infra.database.Purchase;
import com.example.infra.database.User;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class SalesServiceTest extends UnitTest {
  @InjectMocks
  private SalesService salesService;
  @Mock
  private ReadPurchaseStore readPurchaseStore;

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

      List<UserByYearAndMonth> results = salesService.getSalesRecordsByUser(REQUEST_YEAR);

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

      List<ProductByYearAndMonth> results = salesService.getSalesRecordsByProduct(REQUEST_YEAR);

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