package com.example.application.port.out;

import com.example.domain.PurchaseDomain.PurchaseInfo;
import java.util.List;

public interface ReadPurchaseStore {
  List<PurchaseInfo> getPurchasesByYear(int year);
}
