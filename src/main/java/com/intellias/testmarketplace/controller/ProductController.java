package com.intellias.testmarketplace.controller;

import com.intellias.testmarketplace.model.Product;
import com.intellias.testmarketplace.service.ProductService;
import com.intellias.testmarketplace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping(path = "/products")
public class ProductController {
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getProducts(Model model) {
        Set<Product> products = productService.findAll();
        model.addAttribute("products", products);
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
        return "product";    }

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
        model.addObject("products", products);
        model.setViewName("products");
        return model;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable UUID id, ModelMap model) {
        productService.delete(id);
        Set<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "products";
    }

    //@InitBinder
    //public void initBinder(WebDataBinder binder) {
    //    binder.registerCustomEditor(Manufacturer.class, "manufacturer", new ManufacturerEditor(userService));
    //}
}
