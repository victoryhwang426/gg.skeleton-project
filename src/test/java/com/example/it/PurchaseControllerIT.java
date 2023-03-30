package com.example.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.adapter.in.dto.PurchaseDto;
import com.example.adapter.in.dto.PurchaseDto.BuyProduct;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
public class PurchaseControllerIT {
  @Autowired
  private MockMvc mockMvc;
  private final String CONTEXT = "/purchases";
  private ObjectMapper objectMapper = new ObjectMapper();

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
