package com.boostmytool.beststore.models;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class productDto {
    @NotEmpty(message = "name is required")
    private String name;

    @NotEmpty(message = "brand is required")
    private String brand;

    @NotEmpty(message = "category is required")
    private String category;

    @NotEmpty(message = "price is required")
    private double price;

    @Size(min = 10,message = "The description should be atleast 10 characters")
    @Size(max = 1000,message = "The description cannot exceed 1000 characters")
    private String description;

    private MultipartFile imageFileName;

}
