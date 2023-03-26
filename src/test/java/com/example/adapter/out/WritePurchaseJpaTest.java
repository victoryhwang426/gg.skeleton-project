package com.example.adapter.out;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.UnitTest;
import com.example.adapter.out.mapper.PurchaseOutMapper;
import com.example.domain.PurchaseDomain.PurchaseInfo;
import com.example.domain.PurchaseDomain.RegisterPurchaseCommand;
import com.example.infra.database.Purchase;
import com.example.infra.database.repository.PurchaseRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class WritePurchaseJpaTest extends UnitTest {
  @InjectMocks
  private WritePurchaseJpa writePurchaseJpa;
  @Mock
  private PurchaseRepository purchaseRepository;
  @Mock
  private PurchaseOutMapper purchaseOutMapper;

  @Nested
  class savePurchases {
    @Test
    @DisplayName("등록커맨드를 가지고 신규엔티티를 생성 후 DB에 저장한다")
    void test1(){
      Purchase newPurchase = mock(Purchase.class);
      when(purchaseOutMapper.makePurchase(any())).thenReturn(newPurchase);

      Purchase savedPurchase = mock(Purchase.class);
      when(purchaseRepository.saveAll(any())).thenReturn(List.of(savedPurchase));

      PurchaseInfo purchaseInfo = mock(PurchaseInfo.class);
      when(purchaseOutMapper.toPurchaseInfo(any())).thenReturn(purchaseInfo);

      RegisterPurchaseCommand command = mock(RegisterPurchaseCommand.class);
      List<PurchaseInfo> results = writePurchaseJpa.savePurchases(List.of(command));

      verify(purchaseOutMapper).makePurchase(eq(command));
      verify(purchaseRepository).saveAll(eq(List.of(newPurchase)));
      verify(purchaseOutMapper).toPurchaseInfo(eq(savedPurchase));

      assertThat(results.size()).isOne();
      assertThat(results).contains(purchaseInfo);
    }
  }
}