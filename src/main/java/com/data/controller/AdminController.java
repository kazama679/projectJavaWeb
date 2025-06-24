package com.data.controller;

import com.data.entity.Admin;
import com.data.entity.Invoice;
import com.data.repository.AdminRepository;
import com.data.service.CustomerService;
import com.data.service.InvoiceDetailService;
import com.data.service.InvoiceService;
import com.data.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AdminController {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InvoiceService invoiceService;

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
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }
        model.addAttribute("customers", customerService.findAll().size());
        model.addAttribute("products", productService.findAll().size());
        model.addAttribute("invoiceNumber", invoiceService.findAll().size());
        List<Invoice> invoices = invoiceService.findAll();
        Collections.reverse(invoices);
        List<Invoice> firstFour = invoices.subList(0, Math.min(4, invoices.size()));
        model.addAttribute("invoices", firstFour);
        model.addAttribute("revenue", invoiceService.findAll().stream().mapToDouble(i->i.getTotal_amount()).sum());

        // lọc các tháng
        Map<Integer, Double> monthlyTotals = new HashMap<>();
        for (int month = 1; month <= 12; month++) {
            int finalMonth = month;
            double total = invoices.stream()
                    .filter(item -> {
                        LocalDateTime time = item.getCreated_at()
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime();
                        return time.getYear() == 2025 && time.getMonthValue() == finalMonth;
                    })
                    .mapToDouble(Invoice::getTotal_amount)
                    .sum();
            monthlyTotals.put(month, total);
        }

        for (int month = 1; month <= 12; month++) {
            model.addAttribute("t" + month, monthlyTotals.getOrDefault(month, 0.0));
        }

        // Doanh thu từng ngày trong tháng 6/2025
        Map<Integer, Double> dailyRevenue = new HashMap<>();
        for (int day = 1; day <= 30; day++) {
            LocalDateTime start = LocalDate.of(2025, 6, day).atStartOfDay();
            LocalDateTime end = start.plusDays(1);
            double total = invoices.stream()
                    .filter(inv -> {
                        LocalDateTime createdDate = inv.getCreated_at()
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime();
                        return !createdDate.isBefore(start) && createdDate.isBefore(end);
                    })
                    .mapToDouble(Invoice::getTotal_amount)
                    .sum();
            dailyRevenue.put(day, total);
        }

        for (int i = 1; i <= 30; i++) {
            model.addAttribute("d" + i, dailyRevenue.getOrDefault(i, 0.0));
        }

        return "admin/dashboard";
    }
}