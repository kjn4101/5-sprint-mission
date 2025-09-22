package com.codeit.transaction.repository;

import com.codeit.transaction.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
