package com.example.application.port;

import com.example.application.port.in.SalesUseCases;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SalesService implements SalesUseCases {
  @Override
  @Transactional(readOnly = true)
  public void getSalesRecordsByUser(LocalDate year) {

  }

  @Override
  @Transactional(readOnly = true)
  public void getSalesRecordsByProduct(LocalDate year) {

  }
}
