package com.example.adapter.in;

import com.example.adapter.in.dto.ProductDto;
import com.example.adapter.in.dto.ProductDto.Modify;
import com.example.adapter.in.mapper.ProductMapper;
import com.example.application.port.in.ProductUseCases;
import com.example.common.ApiResponse;
import com.example.common.ApiResponse.ProcessStatus;
import com.example.domain.ProductDomain.ModifyProductCommand;
import com.example.domain.ProductDomain.RegisterProductCommand;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
  private final ProductUseCases productUseCases;
  private final ProductMapper productMapper;

  @PostMapping
  public ResponseEntity<?> registerProduct(@RequestBody @Valid ProductDto.Register register){
    RegisterProductCommand command = productMapper.makeRegisterProductCommand(register);
    return ResponseEntity.ok(new ApiResponse<>(ProcessStatus.FINISHED, productUseCases.registerProduct(command)));
  }

  @GetMapping
  public ResponseEntity<?> getProducts(){
    return ResponseEntity.ok(new ApiResponse<>(ProcessStatus.FINISHED, productUseCases.getAllProducts()));
  }

  @GetMapping("/{productId}")
  public ResponseEntity<?> getProduct(@PathVariable long productId){
    return ResponseEntity.ok(new ApiResponse<>(ProcessStatus.FINISHED, productUseCases.getProductBy(productId)));
  }

  @PatchMapping("/{productId}/modify")
  public ResponseEntity<?> modifyProduct(@PathVariable long productId, @RequestBody @Valid Modify modify){
    ModifyProductCommand command = productMapper.makeModifyProductCommand(productId, modify);
    return ResponseEntity.ok(new ApiResponse<>(ProcessStatus.FINISHED, productUseCases.modifyProduct(command)));
  }

  @PatchMapping("/{productId}/delete")
  public ResponseEntity<?> deleteProduct(@PathVariable long productId){
    productUseCases.deleteProduct(productId);
    return ResponseEntity
      .ok(ApiResponse.builder()
        .processStatus(ProcessStatus.FINISHED)
        .build());

  }
}
