package com.example.application.port.in;

import com.example.domain.ProductDomain.ModifyProductCommand;
import com.example.domain.ProductDomain.ProductInfo;
import com.example.domain.ProductDomain.RegisterProductCommand;
import java.util.List;

public interface ProductUseCases {
  List<ProductInfo> getAllProducts();
  ProductInfo getProductBy(long productId);
  ProductInfo registerProduct(RegisterProductCommand command);
  ProductInfo modifyProduct(ModifyProductCommand command);
  void deleteProduct(long productId);
}
