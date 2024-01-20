package com.company.products.controller;

import com.company.products.controller.model.CreateProductRestModel;
import com.company.products.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody CreateProductRestModel createProductRestModel) {

        String productId = productService.createProduct(createProductRestModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }
}
