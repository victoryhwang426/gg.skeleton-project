package com.example.adapter.out;

import com.example.adapter.out.mapper.PurchaseOutMapper;
import com.example.application.port.out.WritePurchaseStore;
import com.example.domain.PurchaseDomain.PurchaseInfo;
import com.example.domain.PurchaseDomain.RegisterPurchaseCommand;
import com.example.infra.database.Purchase;
import com.example.infra.database.repository.PurchaseRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class WritePurchaseJpa implements WritePurchaseStore {
  private final PurchaseRepository purchaseRepository;
  private final PurchaseOutMapper purchaseOutMapper;

  // Todo. 상품명, 회원명이 null 이다
  @Override
  @Transactional
  public List<PurchaseInfo> savePurchases(List<RegisterPurchaseCommand> commands) {
    List<Purchase> newPurchases = commands.stream()
      .map(purchaseOutMapper::makePurchase)
      .collect(Collectors.toList());
    return purchaseRepository.saveAll(newPurchases).stream()
      .map(purchaseOutMapper::toPurchaseInfo)
      .collect(Collectors.toList());
  }
}
