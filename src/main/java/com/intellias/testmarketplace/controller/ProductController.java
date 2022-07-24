package com.intellias.testmarketplace.controller;

import com.intellias.testmarketplace.model.ErrorMessage;
import com.intellias.testmarketplace.model.Message;
import com.intellias.testmarketplace.model.Product;
import com.intellias.testmarketplace.model.User;
import com.intellias.testmarketplace.service.ProductService;
import com.intellias.testmarketplace.service.UserService;
import com.intellias.testmarketplace.validator.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping(path = "/products")
public class ProductController {
    private final ProductService productService;
    private final UserService userService;
    private final ProductValidator validatorService;

    @Autowired
    public ProductController(ProductService productService, UserService userService, ProductValidator validatorService) {
        this.productService = productService;
        this.userService = userService;
        this.validatorService = validatorService;
    }

    @Autowired
    @Qualifier("productValidator")
    private Validator validator;

    @InitBinder("product")
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getProducts(Model model) {
        Set<Product> products = productService.findAll();
        boolean checkout = false;
        model.addAttribute("products", products);
        model.addAttribute("checkout", checkout);
        return "products";
    }

    @RequestMapping(path = "/checkout", method = RequestMethod.GET)
    public String getProductsOnCheckout(Model model, Authentication authentication) {
        User authUser = userService.findByUsername(authentication.getName());
        Set<Product> products = productService.findByUserId(authUser.getId());
        boolean checkout = true;
        model.addAttribute("products", products);
        model.addAttribute("checkout", checkout);
        return "products";
    }


    @RequestMapping(path = "/new", method = RequestMethod.GET)
    public String showNewForm(Model model) {
        model.addAttribute("product", new Product());
        return "product";
    }

    @RequestMapping(path = "/edit/{id}", method = RequestMethod.GET)
    public String showEditForm(@PathVariable UUID id, Model model) {
        model.addAttribute("product", productService.findById(id));
        return "product";
    }

    @RequestMapping(path = "/edit", method = RequestMethod.GET)
    public String showEditFormWithParam(@RequestParam UUID id, Model model) {
        model.addAttribute("product", productService.findById(id));
        return "product";
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView submit(@Valid @ModelAttribute("product") Product product,
                               BindingResult result, ModelAndView model) {
        if (result.hasErrors()) {
            model.addObject("product", product);
            model.setViewName("product");
            model.setStatus(HttpStatus.BAD_REQUEST);
            return model;
        }
        productService.save(product);
        Set<Product> products = productService.findAll();
        boolean checkout = false;
        model.addObject("products", products);
        model.addObject("checkout", checkout);
        model.setViewName("products");
        return model;
    }

    @RequestMapping(value = "/buy/{id}", method = RequestMethod.GET)
    public String buy(@PathVariable UUID id, ModelMap model, Authentication authentication) {
        User authUser = userService.findByUsername(authentication.getName());
        Product product = productService.findById(id);
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage = validatorService.validateUserMoney(authUser, product, errorMessage);
        errorMessage = validatorService.validateAlreadyBoughtProduct(authUser, product, errorMessage);

        if (!errorMessage.getErrors().isEmpty()) {
            model.addAttribute("errorMessage", errorMessage);
        } else {
            Set<Product> products = productService.findByUserId(authUser.getId());
            products.add(product);
            authUser.setProducts(products);
            authUser.setMoney(authUser.getMoney().subtract(product.getPrice()));
            userService.save(authUser);
        }
        Message message = new Message();
        List<String> messages = new ArrayList<>();
        messages.add(String.format("You've successfully add %s to the checkout", product.getName()));
        message.setMessages(messages);
        Set<Product> products = productService.findAll();
        boolean checkout = false;
        model.addAttribute("products", products);
        model.addAttribute("checkout", checkout);
        model.addAttribute("message", message);
        return "products";
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
    public String remove(@PathVariable UUID id, ModelMap model, Authentication authentication) {
        User authUser = userService.findByUsername(authentication.getName());
        Product product = productService.findById(id);
        Set<Product> products = productService.findByUserId(authUser.getId());
        products.remove(product);
        authUser.setProducts(products);
        authUser.setMoney(authUser.getMoney().add(product.getPrice()));
        userService.save(authUser);
        products = productService.findByUserId(authUser.getId());
        Message message = new Message();
        List<String> messages = new ArrayList<>();
        messages.add(String.format("You've successfully removed %s from the checkout", product.getName()));
        message.setMessages(messages);
        boolean checkout = true;
        model.addAttribute("products", products);
        model.addAttribute("checkout", checkout);
        model.addAttribute("message", message);
        return "products";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable UUID id, ModelMap model) {
        productService.delete(id);
        Set<Product> products = productService.findAll();
        boolean checkout = false;
        model.addAttribute("products", products);
        model.addAttribute("checkout", checkout);
        return "products";
    }

    //@InitBinder
    //public void initBinder(WebDataBinder binder) {
    //    binder.registerCustomEditor(Manufacturer.class, "manufacturer", new ManufacturerEditor(userService));
    //}
}
