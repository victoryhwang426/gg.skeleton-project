package com.example.application.port.in;

import java.time.LocalDate;

public interface SalesUseCases {
  void getSalesRecordsByUser(LocalDate year);
  void getSalesRecordsByProduct(LocalDate year);
}
