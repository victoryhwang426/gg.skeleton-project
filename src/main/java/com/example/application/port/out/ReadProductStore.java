package com.example.application.port.out;

import com.example.domain.ProductDomain.ProductInfo;
import java.util.List;
import java.util.Optional;

public interface ReadProductStore {
  Optional<ProductInfo> findProductInfoByProductId(long productId);
  List<ProductInfo> getAllProducts();
  List<ProductInfo> getProductsByProductIds(List<Long> productIds);
}
