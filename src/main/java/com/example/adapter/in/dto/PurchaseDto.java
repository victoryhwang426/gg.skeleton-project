package com.example.adapter.in.dto;

import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class PurchaseDto {
  @Getter
  @Setter
  @ToString
  public static class BuyProduct {
    @Min(value = 1, message = "사용자번호는 0보다 커야합니다.")
    private long userId;
    @NotEmpty(message = "구매할 상품아이디가 존재하지 않습니다.")
    private List<Long> items;
  }
}
