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
    @Min(value = 1, message = "The user number must be greater than 0.")
    private long userId;
    @NotEmpty(message = "The product ID to purchase does not exist.")
    private List<Long> items;
  }
}
