package com.example.infra.database.repository;

import com.example.infra.database.Purchase;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
  List<Purchase> findByYearOfCreatedAt(int year);
}
