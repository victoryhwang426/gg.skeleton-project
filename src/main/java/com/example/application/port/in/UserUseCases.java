package com.example.application.port.in;

import com.example.domain.PurchaseDomain.PurchaseInfo;
import com.example.domain.UserDomain.BuyPurchaseCommand;
import java.util.List;

public interface UserUseCases {
  List<PurchaseInfo> buyProduct(BuyPurchaseCommand command);
}
