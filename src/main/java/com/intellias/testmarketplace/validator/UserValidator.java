package com.intellias.testmarketplace.validator;

import com.intellias.testmarketplace.model.ErrorMessage;
import com.intellias.testmarketplace.model.Role;
import com.intellias.testmarketplace.model.User;
import com.intellias.testmarketplace.repository.RoleRepository;
import com.intellias.testmarketplace.repository.UserRepository;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.validation.*;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component("userValidator")
public class UserValidator implements Validator {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final javax.validation.Validator validator;

    @Autowired
    public UserValidator(UserRepository userRepository, RoleRepository roleRepository, javax.validation.Validator validator) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.validator = validator;
    }

    @Override
    public boolean supports(Class<?> paramClass) {
        return User.class.equals(paramClass);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Set<ConstraintViolation<Object>> validates = validator.validate(obj);

        for (ConstraintViolation<Object> constraintViolation : validates) {
            String propertyPath = constraintViolation.getPropertyPath().toString();
            String message = constraintViolation.getMessage();
            errors.rejectValue(propertyPath, "", message);
        }

        User user = (User) obj;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.required", "Enter username");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "firstName.required", "Enter user first name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "lastName.required", "Enter user last name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required", "Enter user password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "money", "money.required", "Enter user money");

        User userToFind = userRepository.findByUsername(user.getUsername()).orElse(new User());

        if (Objects.isNull(user.getId()) && Objects.nonNull(userToFind.getId())) {
            errors.rejectValue("username", "username.already.exist", "user with this username already exists");
        }

        if (Objects.nonNull(user.getRoles()) && user.getRoles().isEmpty()) {
            errors.rejectValue("roles", "role.required", "role must be assigned");
        }

        Role adminRole = roleRepository.getAdminRole();
        if (Objects.nonNull(user.getId())
                && Objects.nonNull(user.getRoles())
                && !user.getRoles().contains(adminRole)
                && userRepository.findUsersWithAdministratorRole().size() == 1) {
            errors.rejectValue("roles", "admin.required", "at least one admin role must exist");
        }
    }

    public ErrorMessage validateUserToDelete(UUID id) {
        ErrorMessage errorMessage = new ErrorMessage();
        List<String> errors = new ArrayList<>();
        Set<User> admins = userRepository.findUsersWithAdministratorRole();
        User userToDelete = userRepository.findById(id).get();
        if (admins.size() == 1 && userToDelete.getRoles().contains(roleRepository.getAdminRole())) {
            errors.add(String.format("User with username %s is the one with Admin role. Impossible to delete last Admin user."
                    , userRepository.findById(id).get().getUsername()));
        }
        errorMessage.setErrors(errors);
        return errorMessage;
    }
}