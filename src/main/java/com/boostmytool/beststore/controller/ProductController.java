package com.boostmytool.beststore.controller;

import com.boostmytool.beststore.models.Product;
import com.boostmytool.beststore.models.ProductDto;
import com.boostmytool.beststore.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping({"","/"})
    public String showProductList(Model model){
        List<Product> products=productService.showProductList();
        model.addAttribute("products",products);
        return "products/index";
    }
    @GetMapping("/create")
    public String showCreatePage(Model model){
        ProductDto productDto=new ProductDto();
        model.addAttribute("productDto",productDto);
        return "products/createProduct";
    }
    @PostMapping("/create")
    public String createProduct(
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult result){
        if(productDto.getImageFile().isEmpty())
            result.addError(new FieldError("productDto","imageFile","The image file is required"));
        if(result.hasErrors())
            return "products/createProduct";

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

        productService.createProduct(product);

        return "redirect:/products";

    }

}
