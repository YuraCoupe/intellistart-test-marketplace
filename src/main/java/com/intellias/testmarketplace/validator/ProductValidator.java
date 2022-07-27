package com.intellias.testmarketplace.validator;

import com.intellias.testmarketplace.model.ErrorMessage;
import com.intellias.testmarketplace.model.Product;
import com.intellias.testmarketplace.model.User;
import com.intellias.testmarketplace.repository.ProductRepository;
import com.intellias.testmarketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

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
            errors.rejectValue("name", "name.already.exists", "Product with this name already exists");
        }
    }

    public ErrorMessage validateUserMoney(User user, Product product, ErrorMessage errorMessage) {
        List<String> errors = new ArrayList<>();
        if (user.getMoney().compareTo(product.getPrice()) == -1) {
            errors.add(String.format("You do not have enough money to buy %s."
                    , product.getName()));
        }
        errorMessage.setErrors(errors);
        return errorMessage;
    }

    public ErrorMessage validateAlreadyBoughtProduct(User user, Product product, ErrorMessage errorMessage) {
        List<String> errors = new ArrayList<>();
        if (user.getProducts().contains(product)) {
            errors.add(String.format("You've already bought %s."
                    , product.getName()));
        }
        errorMessage.setErrors(errors);
        return errorMessage;
    }
}