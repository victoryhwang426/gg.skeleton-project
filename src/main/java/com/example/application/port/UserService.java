package com.example.application.port;

import com.example.application.port.in.UserUseCases;
import com.example.domain.PurchaseDomain.PurchaseInfo;
import com.example.domain.UserDomain.BuyPurchaseCommand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCases {
  @Override
  @Transactional
  public List<PurchaseInfo> buyProduct(BuyPurchaseCommand command) {
    return null;
  }
}
