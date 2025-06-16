package com.data.controller;

import com.data.entity.Admin;
import com.data.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class AdminController {
    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        session.invalidate();
        model.addAttribute("courses", adminRepository.findAll());
        return "admin/login";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        Optional<Admin> admin = adminRepository.findAll().stream()
                .filter(a -> a.getUsername().equals(username) && a.getPassword().equals(password))
                .findFirst();
        if (username == null || username.trim().isEmpty() ) {
            model.addAttribute("errorUsername", "Please enter both username...");
            model.addAttribute("password", password);
            return "admin/login";
        }
        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("errorPassword", "Please enter both password...");
            model.addAttribute("username", username);
            return "admin/login";
        }
        if(admin.isPresent()){
            session.setAttribute("admin", admin.get());
            return "redirect:/dashboard";
        }
        model.addAttribute("error", "Tài khoản hoặc mật khẩu không chính xác!");
        return "admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }
        return "admin/dashboard";
    }
}