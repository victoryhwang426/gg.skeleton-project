package com.example.adapter.out.mapper;

import com.example.domain.PurchaseDomain.PurchaseInfo;
import com.example.domain.PurchaseDomain.RegisterPurchaseCommand;
import com.example.infra.database.Purchase;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOutMapper {
  public Purchase makePurchase(RegisterPurchaseCommand command){
    return new Purchase(command);
  }

  public PurchaseInfo toPurchaseInfo(Purchase entity){
    return new PurchaseInfo(entity);
  }
}
