package com.example.application.port;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.example.UnitTest;
import com.example.application.port.out.ReadProductStore;
import com.example.application.port.out.WritePurchaseStore;
import com.example.common.Status4xxException;
import com.example.domain.ProductDomain.ProductInfo;
import com.example.domain.PurchaseDomain.PurchaseInfo;
import com.example.domain.PurchaseDomain.RegisterPurchaseCommand;
import com.example.domain.UserDomain.BuyPurchaseCommand;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class UserServiceTest extends UnitTest {
  @InjectMocks
  private UserService userService;
  @Mock
  private WritePurchaseStore writePurchaseStore;
  @Mock
  private ReadProductStore readProductStore;

  @Nested
  class buyProduct {
    @Captor
    private ArgumentCaptor<List<RegisterPurchaseCommand>> captor;
    private final long userId = 12312127L;
    private final long productId = 192087123L;
    private final int price = 12312387;

    @Test
    @DisplayName("구매요청한 상품이 존재하지 않으면 예외를 발생시킨다")
    void test1(){
      ProductInfo productInfo = mock(ProductInfo.class);
      when(readProductStore.getProductsByProductIds(anyList())).thenReturn(List.of(productInfo));
      when(productInfo.getProductId()).thenReturn(12398172937123L);

      BuyPurchaseCommand command = new BuyPurchaseCommand(userId, List.of(productId));
      assertThrows(Status4xxException.class, () -> {
        userService.buyProduct(command);
      });

      verify(readProductStore).getProductsByProductIds(eq(command.getItems()));
      verifyNoInteractions(writePurchaseStore);
    }

    @Test
    @DisplayName("사용자 구매요청정보를 구매커맨드 도메인으로 변환후 구매등록 인터페이스를 호출한다")
    void test2(){
      ProductInfo productInfo = mock(ProductInfo.class);
      when(readProductStore.getProductsByProductIds(anyList())).thenReturn(List.of(productInfo));
      when(productInfo.getProductId()).thenReturn(productId);
      when(productInfo.getPrice()).thenReturn(price);

      PurchaseInfo purchaseInfo = mock(PurchaseInfo.class);
      when(writePurchaseStore.savePurchases(any())).thenReturn(List.of(purchaseInfo));

      BuyPurchaseCommand command = new BuyPurchaseCommand(userId, List.of(productId));
      List<PurchaseInfo> results = userService.buyProduct(command);

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
}