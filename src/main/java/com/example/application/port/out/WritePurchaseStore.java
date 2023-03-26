package com.example.application.port.out;

import com.example.domain.PurchaseDomain.PurchaseInfo;
import com.example.domain.PurchaseDomain.RegisterPurchaseCommand;
import java.util.List;

public interface WritePurchaseStore {
  List<PurchaseInfo> savePurchases(List<RegisterPurchaseCommand> commands);
}
