package com.intellias.testmarketplace.validator;

import com.intellias.testmarketplace.model.ErrorMessage;
import com.intellias.testmarketplace.model.Product;
import com.intellias.testmarketplace.model.Role;
import com.intellias.testmarketplace.model.User;
import com.intellias.testmarketplace.repository.ProductRepository;
import com.intellias.testmarketplace.repository.RoleRepository;
import com.intellias.testmarketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.swing.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Component("productValidator")
public class ProductValidator implements Validator {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ProductValidator(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public boolean supports(Class<?> paramClass) {
        return Product.class.equals(paramClass);
    }

    @Override
    public void validate(Object obj, Errors errors) {

        Product product = (Product) obj;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required", "Enter product name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "price.required", "Enter product price");

        Product productToFind = productRepository.findByName(product.getName()).orElse(new Product());

        if (Objects.isNull(product.getId()) && Objects.nonNull(productToFind.getId())) {
            errors.rejectValue("name", " name.already.exist", "product with this name already exists");
        }
    }

    public ErrorMessage validateUserToDelete(UUID id) {
        ErrorMessage errorMessage = new ErrorMessage();
        List<String> errors = new ArrayList<>();
        Set<User> admins = userRepository.findUsersWithAdministratorRole();

        if (admins.size() == 1) {
            errors.add(String.format("User with email %s is the one with Admin role. Impossible to delete last Admin user."
                    , userRepository.findById(id).get().getUsername()));
        }
        errorMessage.setErrors(errors);
        return errorMessage;
    }
}