package com.example.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.IntegrationTest;
import com.example.adapter.in.dto.PurchaseDto.BuyProduct;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

public class PurchaseControllerIT extends IntegrationTest {

  private final String CONTEXT = "/purchases";

  @Test
  @Transactional
  void buyProduct() throws Exception {
    BuyProduct buyProduct = new BuyProduct();
    buyProduct.setUserId(1100001);
    buyProduct.setItems(List.of(5000701L));

    mockMvc.perform(post(CONTEXT)
            .content(objectMapper.writeValueAsString(buyProduct))
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @Transactional
  void getUserStatics() throws Exception {
    int year = 2023;
    mockMvc.perform(get(CONTEXT + "/{year}/user", year)
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @Transactional
  void getProductStatics() throws Exception {
    int year = 2023;
    mockMvc.perform(get(CONTEXT + "/{year}/product", year)
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());
  }
}
