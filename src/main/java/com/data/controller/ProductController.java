package com.data.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ProductController {
    @GetMapping("/products")
    public String list(HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }
        return "product/list";
    }
}
