package com.boostmytool.beststore.models;


import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductDto {
    @NotEmpty(message = "name is required")
    private String name;

    @NotEmpty(message = "brand is required")
    private String brand;

    @NotEmpty(message = "category is required")
    private String category;

    @NotNull(message = "price is required")
    @Min(value = 1, message = "Price must be greater than 0")
    private double price;

    @Size(min = 10,message = "The description should be at least 10 characters")
    @Size(max = 1000,message = "The description cannot exceed 1000 characters")
    private String description;

    private MultipartFile imageFile;

}
