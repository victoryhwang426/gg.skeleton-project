package com.example.adapter.in.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class ProductDto {
  @Getter
  @Setter
  @ToString
  public static class Register {
    @NotBlank(message = "The product name does not exist.")
    private String productName;
    @NotNull(message = "The amount must exist.")
    private Integer price;
  }

  @Getter
  @Setter
  @ToString
  public static class Modify {
    @NotBlank(message = "The product name does not exist.")
    private String productName;
    @NotNull(message = "The amount must exist.")
    private Integer price;
  }
}
