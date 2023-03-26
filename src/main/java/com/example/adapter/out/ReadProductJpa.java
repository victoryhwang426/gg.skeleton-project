package com.example.adapter.out;

import com.example.adapter.out.mapper.ProductOutMapper;
import com.example.application.port.out.ReadProductStore;
import com.example.domain.ProductDomain.ProductInfo;
import com.example.infra.database.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReadProductJpa implements ReadProductStore {
  private final ProductRepository productRepository;
  private final ProductOutMapper productOutMapper;

  @Override
  @Transactional(readOnly = true)
  public Optional<ProductInfo> findProductInfoByProductId(long productId) {
    return productRepository.findById(productId)
      .map(productOutMapper::toProductInfo);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProductInfo> getAllProducts() {
    return productRepository.findAll().stream()
      .map(productOutMapper::toProductInfo)
      .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProductInfo> getProductsByProductIds(List<Long> productIds) {
    return productRepository.findAllById(productIds).stream()
      .map(productOutMapper::toProductInfo)
      .collect(Collectors.toList());
  }
}
