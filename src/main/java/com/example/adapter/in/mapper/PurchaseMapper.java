package com.example.adapter.in.mapper;

import com.example.adapter.in.dto.PurchaseDto.BuyProduct;
import com.example.domain.UserDomain.BuyProductCommand;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper {
  public BuyProductCommand makeBuyProductCommand(BuyProduct buyProduct){
    return new BuyProductCommand(buyProduct.getUserId(), buyProduct.getItems());
  }
}
