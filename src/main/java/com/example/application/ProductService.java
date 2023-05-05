package com.example.application;

import com.example.application.port.out.ReadProductStore;
import com.example.application.port.out.WriteProductStore;
import com.example.application.port.in.ProductUseCases;
import com.example.common.ApiResponse.ProcessStatus;
import com.example.common.Status4xxException;
import com.example.domain.ProductDomain.ModifyProductCommand;
import com.example.domain.ProductDomain.ProductInfo;
import com.example.domain.ProductDomain.RegisterProductCommand;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductUseCases {
  private final WriteProductStore writeProductStore;
  private final ReadProductStore readProductStore;
  @Override
  @Transactional(readOnly = true)
  public List<ProductInfo> getAllProducts() {
    return readProductStore.getAllProducts();
  }

  @Override
  @Transactional(readOnly = true)
  public ProductInfo getProductBy(long productId) {
    Optional<ProductInfo> optionalProductInfo = readProductStore.findProductInfoByProductId(productId);
    if(optionalProductInfo.isEmpty()){
      throw new Status4xxException(ProcessStatus.STOPPED_BY_CONDITION, "The product does not exist.");
    }

    return optionalProductInfo.get();
  }

  @Override
  @Transactional
  public ProductInfo registerProduct(RegisterProductCommand command) {
    return writeProductStore.saveProduct(command);
  }

  @Override
  @Transactional
  public ProductInfo modifyProduct(ModifyProductCommand command) {
    Optional<ProductInfo> optionalProductInfo = readProductStore.findProductInfoByProductId(command.getProductId());
    if(optionalProductInfo.isEmpty()){
      throw new Status4xxException(ProcessStatus.STOPPED_BY_CONDITION, "The product does not exist.");
    }

    return writeProductStore.modifyProduct(command);
  }

  @Override
  @Transactional
  public void deleteProduct(long productId) {
    Optional<ProductInfo> optionalProductInfo = readProductStore.findProductInfoByProductId(productId);
    if(optionalProductInfo.isEmpty()){
      throw new Status4xxException(ProcessStatus.STOPPED_BY_CONDITION, "The product does not exist.");
    }

    writeProductStore.removeProduct(productId);
  }
}
