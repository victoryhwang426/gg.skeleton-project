package com.example.application.port;

import com.example.application.port.in.SalesUseCases;
import com.example.application.port.out.ReadPurchaseStore;
import com.example.domain.PurchaseDomain.PurchaseInfo;
import com.example.domain.StaticDomain.ProductByYearAndMonth;
import com.example.domain.StaticDomain.UserByYearAndMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SalesService implements SalesUseCases {
  private final ReadPurchaseStore readPurchaseStore;

  @Override
  @Transactional(readOnly = true)
  public List<UserByYearAndMonth> getSalesRecordsByUser(int requestYear) {
    List<PurchaseInfo> purchaseInfos = readPurchaseStore.getPurchasesByYear(requestYear);

    Map<Long, String> userInfoMap = purchaseInfos.stream()
      .collect(Collectors.toMap(
        PurchaseInfo::getUserId,
        PurchaseInfo::getUserName,
        (oldValue, newValue) -> newValue
      ));
    return purchaseInfos
      .stream()
      .collect(Collectors.groupingBy(PurchaseInfo::getKeyByUserAndYearAndMonth))
      .entrySet()
      .stream()
      .map(e -> {
        String[] keys = e.getKey().split("_");
        long userId = Long.parseLong(keys[0]);
        String userName = userInfoMap.get(userId);
        int year = Integer.parseInt(keys[1]);
        int month = Integer.parseInt(keys[2]);
        long totalAmount = e.getValue().stream().mapToLong(PurchaseInfo::getPrice).sum();
        return new UserByYearAndMonth(userId, userName, year, month, totalAmount);
      }).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProductByYearAndMonth> getSalesRecordsByProduct(int requestYear) {
    List<PurchaseInfo> purchaseInfos = readPurchaseStore.getPurchasesByYear(requestYear);

    Map<Long, String> productInfoMap = purchaseInfos.stream()
      .collect(Collectors.toMap(
        PurchaseInfo::getProductId,
        PurchaseInfo::getProductName,
        (oldValue, newValue) -> newValue
      ));
    return purchaseInfos
      .stream()
      .collect(Collectors.groupingBy(PurchaseInfo::getKeyByProductAndYearAndMonth))
      .entrySet()
      .stream()
      .map(e -> {
        String[] keys = e.getKey().split("_");
        long productId = Long.parseLong(keys[0]);
        String productName = productInfoMap.get(productId);
        int year = Integer.parseInt(keys[1]);
        int month = Integer.parseInt(keys[2]);
        long totalAmount = e.getValue().stream().mapToLong(PurchaseInfo::getPrice).sum();
        int totalUserCount = e.getValue().stream().map(PurchaseInfo::getUserId).collect(Collectors.toSet()).size();
        return new ProductByYearAndMonth(productId, productName, year, month, totalAmount, totalUserCount);
      }).collect(Collectors.toList());
  }
}
