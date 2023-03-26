package com.example.adapter.out;

import com.example.adapter.out.mapper.ProductOutMapper;
import com.example.application.port.out.WriteProductStore;
import com.example.domain.ProductDomain.ModifyProductCommand;
import com.example.domain.ProductDomain.ProductInfo;
import com.example.domain.ProductDomain.RegisterProductCommand;
import com.example.infra.database.Product;
import com.example.infra.database.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class WriteProductJpa implements WriteProductStore {
  private final ProductRepository productRepository;
  private final ProductOutMapper productOutMapper;

  @Override
  @Transactional
  public ProductInfo saveProduct(RegisterProductCommand command) {
    Product newEntity = productOutMapper.makeProduct(command);
    return productOutMapper.toProductInfo(productRepository.save(newEntity));
  }

  @Override
  @Transactional
  public void removeProduct(long productId) {
    productRepository.deleteById(productId);
  }

  @Override
  @Transactional
  public ProductInfo modifyProduct(ModifyProductCommand command) {
    Product product = this.getProduct(command.getProductId());
    product.updateProduct(command);
    return productOutMapper.toProductInfo(product);
  }

  @Transactional
  public Product getProduct(long productId){
    return productRepository.getReferenceById(productId);
  }
}
