package com.example.application.port.in;

import java.time.LocalDate;

public interface SalesUseCases {
  void getSalesRecordsByUser(int year);
  void getSalesRecordsByProduct(int year);
}
