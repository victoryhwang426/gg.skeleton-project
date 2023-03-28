package com.example.application;

import com.example.application.port.out.ReadProductStore;
import com.example.application.port.out.ReadPurchaseStore;
import com.example.application.port.out.ReadUserStore;
import com.example.application.port.out.WritePurchaseStore;
import com.example.application.port.in.PurchaseUseCases;
import com.example.common.ApiResponse.ProcessStatus;
import com.example.common.Status4xxException;
import com.example.domain.ProductDomain.ProductInfo;
import com.example.domain.PurchaseDomain.PurchaseInfo;
import com.example.domain.PurchaseDomain.RegisterPurchaseCommand;
import com.example.domain.StaticDomain.ProductByYearAndMonth;
import com.example.domain.StaticDomain.UserByYearAndMonth;
import com.example.domain.UserDomain.BuyProductCommand;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseService implements PurchaseUseCases {
  private final WritePurchaseStore writePurchaseStore;
  private final ReadProductStore readProductStore;
  private final ReadPurchaseStore readPurchaseStore;
  private final ReadUserStore readUserStore;

  @Override
  @Transactional
  public List<PurchaseInfo> buyProduct(BuyProductCommand command) {
    boolean hasDuplicatedItem = !command.getItems()
      .stream()
      .allMatch(new HashSet<>()::add);
    if(hasDuplicatedItem) {
      throw new Status4xxException(
        ProcessStatus.STOPPED_BY_CONDITION,
        "중복된 상품아이디가 존재합니다."
      );
    }

    readUserStore.findUserByUserId(command.getUserId()).orElseThrow(() -> {
      throw new Status4xxException(
        ProcessStatus.STOPPED_BY_CONDITION,
        "사용자가 존재하지 않습니다."
      );
    });

    Map<Long, ProductInfo> productInfoMap =
      readProductStore.getProductsByProductIds(command.getItems()).stream()
        .collect(Collectors.toMap(ProductInfo::getProductId, Function.identity()));
    List<RegisterPurchaseCommand> commands = command.getItems().stream()
      .map(productId -> {
        ProductInfo productInfo = productInfoMap.getOrDefault(productId, null);
        if(productInfo == null) {
          throw new Status4xxException(
            ProcessStatus.STOPPED_BY_CONDITION,
            "상품정보가 존재하지 않습니다."
          );
        }
        return new RegisterPurchaseCommand(command.getUserId(), productInfo);
      }).collect(Collectors.toList());

    return writePurchaseStore.savePurchases(commands);
  }

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
