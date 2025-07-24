package com.boostmytool.beststore.controller;

import com.boostmytool.beststore.dto.ProductRepository;
import com.boostmytool.beststore.models.Product;
import com.boostmytool.beststore.models.ProductDto;
import com.boostmytool.beststore.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;
    @Autowired
    private ProductRepository productRepository;

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
        productService.createProduct(productDto);

        return "redirect:/products";

    }
    @GetMapping("/edit")
    public String showEditPage(Model model,@RequestParam int id){
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            model.addAttribute("product", product);

            ProductDto productDto = new ProductDto();
            productDto.setName(product.getName());
            productDto.setBrand(product.getBrand());
            productDto.setCategory(product.getCategory());
            productDto.setPrice(product.getPrice());
            productDto.setDescription(product.getDescription());

            // Make sure to add the dto to the model properly
            model.addAttribute("productDto", productDto);

            return "products/EditProduct";
        } else {
            // Handle product not found gracefully
            return "redirect:/products?error=notfound";
        }

    }
    @PostMapping("/edit")
    public String updateProduct(
            Model model, @RequestParam int id, @Valid @ModelAttribute ProductDto productDto, BindingResult result){
             Optional<Product> productOptional = productRepository.findById(id);
            try {
                Product product = productOptional.get();
                model.addAttribute("product", product);

                if(result.hasErrors())
                    return "products/EditProduct";

                if (!productDto.getImageFile().isEmpty()) {
                    // delete old image
                    String uploadDir = "public/images/";
                    Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());

                    try {
                        Files.delete(oldImagePath);
                    }
                    catch(Exception ex) {
                        System.out.println("Exception: " + ex.getMessage());
                    }

                    // save new image file
                    MultipartFile image = productDto.getImageFile();
                    Date createdAt = new Date();
                    String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

                    try (InputStream inputStream = image.getInputStream()) {
                        Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                                StandardCopyOption.REPLACE_EXISTING);
                    }

                    product.setImageFileName(storageFileName);


                }
                product.setName(productDto.getName());
                product.setBrand(productDto.getBrand());
                product.setCategory(productDto.getCategory());
                product.setPrice(productDto.getPrice());
                product.setDescription(productDto.getDescription());
                productRepository.save(product);

            }catch (Exception e){
                System.out.println("Exception:"+ e.getMessage());
            }
        return "redirect:/products";
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam int id){
        productService.deleteProduct(id);
        return "redirect:/products";
    }

}
