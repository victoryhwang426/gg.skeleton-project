package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
public abstract class UnitTest {
  protected ObjectMapper objectMapper = new ObjectMapper();
  protected MockMvc mockMvc;
}
