package com.example.adapter.in.mapper;

import com.example.adapter.in.dto.ProductDto.Modify;
import com.example.adapter.in.dto.ProductDto.Register;
import com.example.domain.ProductDomain.ModifyProductCommand;
import com.example.domain.ProductDomain.RegisterProductCommand;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
  public RegisterProductCommand makeRegisterProductCommand(Register register){
    return new RegisterProductCommand(register.getProductName(), register.getPrice());
  }

  public ModifyProductCommand makeModifyProductCommand(long productId, Modify modify){
    return new ModifyProductCommand(productId, modify.getProductName(), modify.getPrice());
  }
}
