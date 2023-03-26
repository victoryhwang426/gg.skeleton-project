package com.example.adapter.out.mapper;

import com.example.domain.ProductDomain.ProductInfo;
import com.example.domain.ProductDomain.RegisterProductCommand;
import com.example.infra.database.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductOutMapper {
  public Product makeProduct(RegisterProductCommand command){
    return new Product(command);
  }
  public ProductInfo toProductInfo(Product entity){
    return new ProductInfo(entity);
  }
}
