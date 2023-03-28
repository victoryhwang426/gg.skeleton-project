package com.example.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.adapter.in.dto.ProductDto.Modify;
import com.example.adapter.in.dto.ProductDto.Register;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIT {
  @Autowired
  private MockMvc mockMvc;
  private final String CONTEXT = "/products";
  private ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @Transactional
  void registerProduct() throws Exception {
    Register register = new Register();
    register.setProductName(RandomString.make());
    register.setPrice(1231245);
    mockMvc.perform(post(CONTEXT)
        .content(objectMapper.writeValueAsString(register))
        .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andDo(print());
  }

  @Test
  @Transactional
  void getProducts() throws Exception {
    mockMvc.perform(get(CONTEXT)
        .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andDo(print());
  }

  @Test
  @Transactional
  void getProduct() throws Exception {
    long productId = 5000101;
    mockMvc.perform(get(CONTEXT + "/{productId}", productId)
        .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andDo(print());
  }

  @Test
  @Transactional
  void modifyProduct() throws Exception {
    long productId = 5000101;
    Modify modify = new Modify();
    modify.setProductName(RandomString.make());
    modify.setPrice(123235125);
    mockMvc.perform(patch(CONTEXT+"/{productId}/modify", productId)
        .content(objectMapper.writeValueAsString(modify))
        .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andDo(print());
  }

  @Test
  @Transactional
  void deleteProduct() throws Exception {
    long productId = 5000101;
    mockMvc.perform(patch(CONTEXT+"/{productId}/delete", productId)
        .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andDo(print());
  }
}
