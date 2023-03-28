package com.example.application.port.in;

import com.example.domain.PurchaseDomain.PurchaseInfo;
import com.example.domain.StaticDomain.ProductByYearAndMonth;
import com.example.domain.StaticDomain.UserByYearAndMonth;
import com.example.domain.UserDomain.BuyProductCommand;
import java.util.List;

public interface PurchaseUseCases {
  List<PurchaseInfo> buyProduct(BuyProductCommand command);
  List<UserByYearAndMonth> getSalesRecordsByUser(int year);
  List<ProductByYearAndMonth> getSalesRecordsByProduct(int year);
}
