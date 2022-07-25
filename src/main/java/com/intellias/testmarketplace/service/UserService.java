package com.intellias.testmarketplace.service;

import com.intellias.testmarketplace.exception.UserNotFoundException;
import com.intellias.testmarketplace.model.User;
import com.intellias.testmarketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Set<User> findAll() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(String.format("User with username - %s does not exist", username)));
    }

    public Set<User> findUsersWithAdministratorRole() {
        return userRepository.findUsersWithAdministratorRole();
    }

    public Set<User> findUsersWithUserRole() {
        return userRepository.findUsersWithUserRole();
    }

    public User findById (UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("User with id - %s does not exist", id)));
    }

    public Set<User> findUsersByProductId(UUID id) {
        return userRepository.findUsersByProductId(id);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void delete(UUID id) {
        userRepository.deleteById(id);
    }





}
