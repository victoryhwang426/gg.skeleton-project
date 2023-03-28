package com.example.adapter.in;

import com.example.adapter.in.dto.PurchaseDto;
import com.example.adapter.in.mapper.PurchaseMapper;
import com.example.application.port.in.PurchaseUseCases;
import com.example.common.ApiResponse;
import com.example.common.ApiResponse.ProcessStatus;
import com.example.domain.StaticDomain.ProductByYearAndMonth;
import com.example.domain.StaticDomain.UserByYearAndMonth;
import com.example.domain.UserDomain.BuyProductCommand;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/purchases")
public class PurchaseController {
  private final PurchaseUseCases purchaseUseCases;
  private final PurchaseMapper purchaseMapper;

  @PostMapping
  public ResponseEntity<?> buyProduct(@RequestBody @Valid PurchaseDto.BuyProduct buyProduct){
    BuyProductCommand command = purchaseMapper.makeBuyProductCommand(buyProduct);
    return ResponseEntity.ok(new ApiResponse<>(ProcessStatus.FINISHED, purchaseUseCases.buyProduct(command)));
  }

  @GetMapping("/{year}/user")
  public ResponseEntity<?> getUserStatics(@PathVariable int year){
    List<UserByYearAndMonth> userByYearAndMonths = purchaseUseCases.getSalesRecordsByUser(year);
    return ResponseEntity.ok(new ApiResponse<>(ProcessStatus.FINISHED, userByYearAndMonths));
  }

  @GetMapping("/{year}/product")
  public ResponseEntity<?> getProductStatics(@PathVariable int year){
    List<ProductByYearAndMonth> productByYearAndMonths = purchaseUseCases.getSalesRecordsByProduct(year);
    return ResponseEntity.ok(new ApiResponse<>(ProcessStatus.FINISHED, productByYearAndMonths));
  }
}

