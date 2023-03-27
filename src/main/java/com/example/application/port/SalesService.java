package com.example.application.port;

import com.example.application.port.in.SalesUseCases;
import com.example.application.port.out.ReadPurchaseStore;
import com.example.domain.PurchaseDomain.PurchaseInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SalesService implements SalesUseCases {
  private final ReadPurchaseStore readPurchaseStore;

  @Override
  @Transactional(readOnly = true)
  public void getSalesRecordsByUser(int year) {
    List<PurchaseInfo> purchaseInfos = readPurchaseStore.getPurchasesByYear(year);
  }

  @Override
  @Transactional(readOnly = true)
  public void getSalesRecordsByProduct(int year) {
    List<PurchaseInfo> purchaseInfos = readPurchaseStore.getPurchasesByYear(year);
  }
}
