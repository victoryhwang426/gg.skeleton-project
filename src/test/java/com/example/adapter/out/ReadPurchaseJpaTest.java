package com.example.adapter.out;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.UnitTest;
import com.example.adapter.out.mapper.PurchaseOutMapper;
import com.example.domain.PurchaseDomain.PurchaseInfo;
import com.example.infra.database.Purchase;
import com.example.infra.database.repository.PurchaseRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class ReadPurchaseJpaTest extends UnitTest {

  @InjectMocks
  private ReadPurchaseJpa readPurchaseJpa;
  @Mock
  private PurchaseRepository purchaseRepository;
  @Mock
  private PurchaseOutMapper purchaseOutMapper;

  @Nested
  class getPurchasesByYear {

    @Test
    @DisplayName("find user's yearly purchase static")
    void test1() {
      Purchase purchase = mock(Purchase.class);
      when(purchaseRepository.findByYearOfCreatedAt(anyInt())).thenReturn(List.of(purchase));

      PurchaseInfo purchaseInfo = mock(PurchaseInfo.class);
      when(purchaseOutMapper.toPurchaseInfo(any())).thenReturn(purchaseInfo);

      int year = 2020;
      List<PurchaseInfo> results = readPurchaseJpa.getPurchasesByYear(year);

      verify(purchaseRepository).findByYearOfCreatedAt(eq(year));
      verify(purchaseOutMapper).toPurchaseInfo(eq(purchase));

      assertThat(results.size()).isOne();
      assertThat(results).contains(purchaseInfo);
    }
  }
}