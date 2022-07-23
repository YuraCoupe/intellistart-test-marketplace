package com.intellias.testmarketplace.validator;

import com.intellias.testmarketplace.model.ErrorMessage;
import com.intellias.testmarketplace.model.Role;
import com.intellias.testmarketplace.model.User;
import com.intellias.testmarketplace.repository.RoleRepository;
import com.intellias.testmarketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.*;

@Component("userValidator")
public class UserValidator implements Validator {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserValidator(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean supports(Class<?> paramClass) {
        return User.class.equals(paramClass);
    }

    @Override
    public void validate(Object obj, Errors errors) {

        User user = (User) obj;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.required", "Enter username");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "firstName.required", "Enter user first name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "lastName.required", "Enter user last name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required", "Enter user password");

        User userToFind = userRepository.findByUsername(user.getUsername()).orElse(new User());

        if (Objects.isNull(user.getId()) && Objects.nonNull(userToFind.getId())) {
            errors.rejectValue("username", "username.already.exist", "user with this username already exists");
        }

        if (Objects.isNull(user.getRoles())) {
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

        if (admins.size() == 1) {
            errors.add(String.format("User with email %s is the one with Admin role. Impossible to delete last Admin user."
                    , userRepository.findById(id).get().getUsername()));
        }
        errorMessage.setErrors(errors);
        return errorMessage;
    }
}