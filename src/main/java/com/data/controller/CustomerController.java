package com.data.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CustomerController {
    @GetMapping("/customers")
    public String list(HttpSession session) {
//        if (session.getAttribute("admin") == null) {
//            return "redirect:/login";
//        }
        return "customer/list";
    }
}
