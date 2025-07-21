package com.boostmytool.beststore.controller;

import com.boostmytool.beststore.models.Product;
import com.boostmytool.beststore.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
