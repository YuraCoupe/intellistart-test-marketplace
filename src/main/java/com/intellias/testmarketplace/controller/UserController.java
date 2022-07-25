package com.intellias.testmarketplace.controller;

import com.intellias.testmarketplace.model.ErrorMessage;
import com.intellias.testmarketplace.model.Role;
import com.intellias.testmarketplace.model.RoleEditor;
import com.intellias.testmarketplace.model.User;
import com.intellias.testmarketplace.service.RoleService;
import com.intellias.testmarketplace.service.UserService;
import com.intellias.testmarketplace.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final UserValidator validatorService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, RoleService roleService, UserValidator validatorService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.validatorService = validatorService;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    @Qualifier("userValidator")
    private Validator validator;

    @InitBinder("user")
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
        binder.registerCustomEditor(Role.class, "roles", new RoleEditor(roleService));
    }

    @ModelAttribute("user")
    public User createUserModel() {
        return new User();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getUsers(Model model) {
        Set<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @RequestMapping(path = "/administrators", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getUsersWithAdministratorRole(Model model) {
        Set<User> users = userService.findUsersWithAdministratorRole();
        model.addAttribute("users", users);
        return "users";
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getUsersWithUserRole(Model model) {
        Set<User> users = userService.findUsersWithUserRole();
        model.addAttribute("users", users);
        return "users";
    }

    @RequestMapping(path = "/new", method = RequestMethod.GET)
    public String showNewForm(Model model) {
        Set<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        //model.addAttribute("manufacturer", new Manufacturer());
        model.addAttribute("user", new User());
        return "user";
    }

    @RequestMapping(path = "/edit/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showEditForm(@PathVariable UUID id, Model model) {
        Set<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        //model.addAttribute("role", new Role());
        User user = userService.findById(id);
        user.setPassword("********");
        model.addAttribute("user", user);
        return "user";
    }

    @RequestMapping(path = "/edit", method = RequestMethod.GET)
    public String showEditFormWithParam(@RequestParam UUID id, Model model) {
        Set<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        //model.addAttribute("role", new Role());
        User user = userService.findById(id);
        user.setPassword("********");
        model.addAttribute("user", user);
        return "user";    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public ModelAndView submit(@Valid @ModelAttribute("user") User user,
                         BindingResult result) {
        ModelAndView model = new ModelAndView();
        if (result.hasErrors()) {
            model.addObject("user", user);
            model.setViewName("user");
            Set<Role> roles = roleService.findAll();
            model.addObject("roles", roles);
            model.setStatus(HttpStatus.BAD_REQUEST);
            return model;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((!(authentication instanceof AnonymousAuthenticationToken) && result.hasErrors())
                || ((authentication instanceof AnonymousAuthenticationToken)) && result.hasErrors() && !result.hasFieldErrors("roles")){
//            if (Objects.nonNull(user.getId())) {
//                user = userService.findById(user.getId());
//            }
            Set<Role> roles = roleService.findAll();
            model.addObject("roles", roles);
            model.addObject("user", user);
            model.setViewName("user");
            model.setStatus(HttpStatus.BAD_REQUEST);
            return model;
        }
        if (user.getPassword().equals("********")) {
            User oldUser = userService.findById(user.getId());
            user.setPassword(oldUser.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (Objects.isNull(user.getRoles())) {
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(roleService.getUserRole());
            user.setRoles(userRoles);
        }
        userService.save(user);
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Set<User> users = userService.findAll();
            model.addObject("users", users);
            model.setViewName("users");
            return model;
        } else {
            model.setViewName("login");
            return model;
        }
    }

    @RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable UUID id, ModelMap model){
        ErrorMessage errorMessage = validatorService.validateUserToDelete(id);
        if (!errorMessage.getErrors().isEmpty()) {
            model.addAttribute("errorMessage", errorMessage);
        } else {
            userService.delete(id);
        }
        Set<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users";
    }
}
