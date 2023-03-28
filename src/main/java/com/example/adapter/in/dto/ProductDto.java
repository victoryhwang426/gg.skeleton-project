package com.example.adapter.in.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class ProductDto {
  @Getter
  @Setter
  @ToString
  public static class Register {
    @NotEmpty(message = "상품명이 존재하지 않습니다.")
    private String productName;
    @NotNull(message = "금액은 존재해야 합니다.")
    private Integer price;
  }

  @Getter
  @Setter
  @ToString
  public static class Modify {
    @NotEmpty(message = "상품명이 존재하지 않습니다.")
    private String productName;
    @NotNull(message = "금액은 존재해야 합니다.")
    private Integer price;
  }
}
