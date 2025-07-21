package com.boostmytool.beststore.dto;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boostmytool.beststore.models.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
}
