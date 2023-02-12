package com.example.project.controllers;

import com.example.project.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.project.entities.Product;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts() {
        try {
            return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") int id) {
        try {
            //check if product exist in database
            Product empObj = getProductRec(id);

            if (empObj != null) {
                return new ResponseEntity<>(empObj, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/product")
    public ResponseEntity<Product> newProduct(@RequestBody Product product) {
        Product newProduct = productRepository
                .save(Product.builder()
                        .name(product.getName())
                        .price(product.getPrice())
                        .build());
        return new ResponseEntity<>(newProduct, HttpStatus.OK);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") int id, @RequestBody Product product) {

        //check if product exist in database
        Product empObj = getProductRec(id);

        if (empObj != null) {
            empObj.setName(product.getName());
            empObj.setPrice(product.getPrice());
            return new ResponseEntity<>(productRepository.save(empObj), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<HttpStatus> deleteProductById(@PathVariable("id") int id) {
        try {
            //check if product exist in database
            Product emp = getProductRec(id);

            if (emp != null) {
                productRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Product getProductRec(int id) {
        Optional<Product> empObj = productRepository.findById(id);

        if (empObj.isPresent()) {
            return empObj.get();
        }
        return null;
    }
}
