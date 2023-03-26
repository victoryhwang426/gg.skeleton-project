package com.example.application.port.out;

import com.example.domain.ProductDomain.ModifyProductCommand;
import com.example.domain.ProductDomain.ProductInfo;
import com.example.domain.ProductDomain.RegisterProductCommand;

public interface WriteProductStore {
  ProductInfo saveProduct(RegisterProductCommand command);
  void removeProduct(long productId);
  ProductInfo modifyProduct(ModifyProductCommand command);
}
