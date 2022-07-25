package com.intellias.testmarketplace.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/")
public class IndexController {

    @GetMapping(path = "/")
    public String getIndexPage() {
        return "index";
    }

    @PostMapping(path = "/")
    public String getIndexPageFromPost() {
        return "index";
    }
}
