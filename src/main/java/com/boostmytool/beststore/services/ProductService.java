package com.boostmytool.beststore.services;

import com.boostmytool.beststore.dto.ProductRepository;
import com.boostmytool.beststore.models.Product;
import com.boostmytool.beststore.models.ProductDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> showProductList() {
       return productRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
    }


    public void createProduct(@Valid ProductDto productDto) {


        MultipartFile image=productDto.getImageFile();
        Date createdAt=new Date();
        String storageFileName=createdAt.getTime()+ " " + image.getOriginalFilename();
        try {
            String uploadDir="public/images/";
            Path uploadpath= Paths.get(uploadDir);
            if(!Files.exists(uploadpath))
                Files.createDirectories(uploadpath);
            try (InputStream inputStream=image.getInputStream()){
                Files.copy(inputStream,Paths.get(uploadDir+storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (Exception e){
            System.out.println("Exception:"+ e.getMessage());
        }
        Product product=new Product();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setCreatedAt(createdAt);
        product.setImageFileName(storageFileName);
        productRepository.save(product);
    }

    public void deleteProduct(int id) {
        try {
            Product product=productRepository.findById(id).get();
            Path imagePath=Paths.get("public/images/"+ product.getImageFileName());
                    try{
                        Files.delete(imagePath);
                    }
                    catch (Exception e){
                        System.out.println("Exception :"+ e.getMessage());
                    }
            if (productRepository.existsById(id)) {
                productRepository.deleteById(id);
            } else {
                throw new EntityNotFoundException("Product not found with id: " + id);
            }

        }
        catch (Exception e){
            System.out.println("Exception :"+ e.getMessage());
        }
    }


}
