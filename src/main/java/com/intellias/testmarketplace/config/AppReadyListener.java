package com.intellias.testmarketplace.config;

import com.intellias.testmarketplace.exception.UserNotFoundException;
import com.intellias.testmarketplace.model.Role;
import com.intellias.testmarketplace.model.User;
import com.intellias.testmarketplace.service.RoleService;
import com.intellias.testmarketplace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class AppReadyListener {
    @Value("${DEFAULT_ADMIN_NAME}")
    private String defaultAdminName;

    @Value("${DEFAULT_ADMIN_PASSWORD}")
    private String defaultAdminPassword;

    private UserService userService;
    private RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
public AppReadyListener(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }


    @EventListener(ApplicationReadyEvent.class)
    public void appReady() {
        try {
            if (userService.findByUsername(defaultAdminName) != null) {
                return;
            }
        } catch (UserNotFoundException e) {
            addAdminUser();
        }
    }

    private void addAdminUser() {
        User adminUser = new User();
        adminUser.setUsername(defaultAdminName);
        adminUser.setPassword(passwordEncoder.encode(defaultAdminPassword));
        adminUser.setFirstName("admin");
        adminUser.setLastName("admin");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleService.getAdminRole());
        adminUser.setRoles(userRoles);

        userService.save(adminUser);
    }
}

