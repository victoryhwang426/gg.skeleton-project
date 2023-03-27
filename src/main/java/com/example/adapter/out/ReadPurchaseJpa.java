package com.example.adapter.out;

import com.example.adapter.out.mapper.PurchaseOutMapper;
import com.example.application.port.out.ReadPurchaseStore;
import com.example.domain.PurchaseDomain.PurchaseInfo;
import com.example.infra.database.repository.PurchaseRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReadPurchaseJpa implements ReadPurchaseStore {
  private final PurchaseRepository purchaseRepository;
  private final PurchaseOutMapper purchaseOutMapper;

  @Override
  @Transactional(readOnly = true)
  public List<PurchaseInfo> getPurchasesByYear(int year) {
    return purchaseRepository.findByYearOfCreatedAt(year).stream()
      .map(purchaseOutMapper::toPurchaseInfo)
      .collect(Collectors.toList());
  }
}
