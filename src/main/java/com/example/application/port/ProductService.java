package com.example.application.port;

import com.example.application.port.in.ProductUseCases;
import com.example.domain.ProductDomain.ModifyProductCommand;
import com.example.domain.ProductDomain.ProductInfo;
import com.example.domain.ProductDomain.RegisterProductCommand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductUseCases {
  @Override
  @Transactional(readOnly = true)
  public List<ProductInfo> getAllProducts() {
    return null;
  }

  @Override
  @Transactional(readOnly = true)
  public ProductInfo getProductBy(long productId) {
    return null;
  }

  @Override
  @Transactional
  public ProductInfo registerProduct(RegisterProductCommand command) {
    return null;
  }

  @Override
  @Transactional
  public ProductInfo modifyProduct(ModifyProductCommand command) {
    return null;
  }

  @Override
  @Transactional
  public void deleteProduct(long productId) {

  }
}
