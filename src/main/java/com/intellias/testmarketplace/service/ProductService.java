package com.intellias.testmarketplace.service;

import com.intellias.testmarketplace.exception.ProductNotFoundException;
import com.intellias.testmarketplace.model.Product;
import com.intellias.testmarketplace.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product findById (UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(String.format("Product with id - %s does not exist", id)));
    }

    public Set<Product> findAll() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .collect(Collectors.toSet());
    }

    public Set<Product> findByUserId(UUID id) {
        return StreamSupport.stream(productRepository.findByUserId(id).spliterator(), false)
                .collect(Collectors.toSet());
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public void delete(UUID id) {
        productRepository.deleteById(id);
    }
}
