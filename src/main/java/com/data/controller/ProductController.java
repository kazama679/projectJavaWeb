package com.data.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.data.dto.ProductDTO;
import com.data.entity.Product;
import com.data.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private Cloudinary cloudinary;

    @GetMapping("/products")
    public String list(@RequestParam(value = "searchProduct", required = false) String search, HttpSession session, Model model) {
//        if (session.getAttribute("admin") == null) {
//            return "redirect:/login";
//        }
        List<Product> products;
        if (search != null && !search.trim().isEmpty()) {
            products = productService.findByBrand(search);
        } else {
            products = productService.findAll();
        }
        model.addAttribute("products", products);
        model.addAttribute("searchProduct", search);
        model.addAttribute("productDTO", new ProductDTO());
        return "product/list";
    }

    @PostMapping("/products")
    public String save(@Valid @ModelAttribute("product") Product product, Model model) {
        Product productNew = new Product();
        productNew.setId(product.getId());
        productNew.setName(product.getName());
        productNew.setBrand(product.getBrand());
        productNew.setPrice(product.getPrice());
        productNew.setStock(product.getStock());
        Map<String, Object> upload;
        try {
            upload = cloudinary.uploader().upload(product.getImage().getBytes(), ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        productNew.setImage(upload.get("url").toString());
        productService.save(productNew);
        return "redirect:/products";
    }
}
