package com.boostmytool.beststore.services;

import com.boostmytool.beststore.dto.ProductRepository;
import com.boostmytool.beststore.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> showProductList() {
       return productRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
    }
}
