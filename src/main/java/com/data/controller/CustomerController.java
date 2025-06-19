package com.data.controller;

import com.data.dto.CustomerDTO;
import com.data.entity.Customer;
import com.data.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public String list(HttpSession session, Model model) {
//        if (session.getAttribute("admin") == null) {
//            return "redirect:/login";
//        }
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        return "customer/list";
    }

    @GetMapping("/customers/add")
    public String add(Model model) {
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        model.addAttribute("customerDTO", new CustomerDTO());
        return "customer/add";
    }

    @PostMapping("/customers/add")
    public String save(@Valid @ModelAttribute("customerDTO") CustomerDTO customer, BindingResult result, Model model) {
        if(customerService.existsByEmail(customer.getEmail())) {
            result.rejectValue("email", "error.customer", "Email đã tồn tại!");
        }
        if(customerService.existsByPhone(customer.getPhone())) {
            result.rejectValue("phone", "error.customer", "Số đã tồn tại!");
        }
        if (result.hasErrors()) {
            model.addAttribute("customerDTO", customer);
            List<Customer> customers = customerService.findAll();
            model.addAttribute("customers", customers);
            return "customer/add";
        }
        Customer customerNew = new Customer();
        customerNew.setId(customer.getId());
        customerNew.setName(customer.getName());
        customerNew.setEmail(customer.getEmail());
        customerNew.setPhone(customer.getPhone());
        customerNew.setAddress(customer.getAddress());
        customerNew.setStatus(customer.isStatus());
        customerService.save(customerNew);
        return "redirect:/customers/";
    }

    @GetMapping("/customers/updateStatus")
    public String updateStatus(int id) {
        Customer customer = customerService.findById(id);
        if (customer != null) {
            customer.setStatus(!customer.isStatus());
            customerService.save(customer);
        }
        return "redirect:/customers";
    }

    @GetMapping("/customers/edit")
    public String edit(@RequestParam("id") int id, Model model) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            return "redirect:/customers";
        }
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setAddress(customer.getAddress());
        dto.setStatus(customer.isStatus());

        model.addAttribute("customerDTO", dto);
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        return "customer/edit";
    }

    @PostMapping("/customers/update")
    public String updateProduct(@Valid @ModelAttribute("customerDTO") CustomerDTO dto,
                                BindingResult result,
                                Model model) {
        if(customerService.existsByEmailEdit(dto.getEmail(), dto.getId())) {
            model.addAttribute("customers", customerService.findAll());
            result.rejectValue("email", "error.customer", "Email đã tồn tại!");
        }
        if(customerService.existsByPhoneEdit(dto.getPhone(), dto.getId())) {
            model.addAttribute("customers", customerService.findAll());
            result.rejectValue("phone", "error.customer", "Số đã tồn tại!");
        }
        if (result.hasErrors()) {
            model.addAttribute("customers", customerService.findAll());
            return "customer/edit";
        }
        Customer customer = new Customer();
        customer.setId(dto.getId());
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        customer.setStatus(dto.isStatus());
        customerService.update(customer);
        return "redirect:/customers";
    }
}