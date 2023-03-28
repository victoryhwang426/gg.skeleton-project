package com.example.application.port.in;

import com.example.domain.StaticDomain.ProductByYearAndMonth;
import com.example.domain.StaticDomain.UserByYearAndMonth;
import java.util.List;

public interface SalesUseCases {
  List<UserByYearAndMonth> getSalesRecordsByUser(int year);
  List<ProductByYearAndMonth> getSalesRecordsByProduct(int year);
}
