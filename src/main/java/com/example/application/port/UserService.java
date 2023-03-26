package com.example.application.port;

import com.example.application.port.in.UserUseCases;
import com.example.application.port.out.ReadProductStore;
import com.example.application.port.out.WritePurchaseStore;
import com.example.common.ApiResponse.ProcessStatus;
import com.example.common.Status4xxException;
import com.example.domain.ProductDomain.ProductInfo;
import com.example.domain.PurchaseDomain.PurchaseInfo;
import com.example.domain.PurchaseDomain.RegisterPurchaseCommand;
import com.example.domain.UserDomain.BuyPurchaseCommand;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCases {
  private final WritePurchaseStore writePurchaseStore;
  private final ReadProductStore readProductStore;
  @Override
  @Transactional
  public List<PurchaseInfo> buyProduct(BuyPurchaseCommand command) {
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
}
