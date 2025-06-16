package com.data.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.data.dto.ProductDTO;
import com.data.entity.Product;
import com.data.service.ProductService;
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
    public String list(
            @RequestParam(value = "searchProduct", required = false) String search,
            @RequestParam(value = "page", defaultValue = "1") int page,
            HttpSession session,
            Model model
    ) {
//        if (session.getAttribute("admin") == null) {
//            return "redirect:/login";
//        }
        List<Product> allProducts;
        if (search != null && !search.trim().isEmpty()) {
            allProducts = productService.findByBrand(search);
        } else {
            allProducts = productService.findAll();
        }
        model.addAttribute("products", allProducts);
        model.addAttribute("searchProduct", search);
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("currentPage", page);
        return "product/list";
    }

    @PostMapping("/products")
    public String save(@Valid @ModelAttribute("productDTO") ProductDTO product,
                       BindingResult result,
                       Model model) {
        if(productService.existsByName(product.getName())) {
            result.rejectValue("name", "error.product", "Tên sản phẩm đã tồn tại!");
        }
        if (product.getImage() == null || product.getImage().isEmpty()) {
            result.rejectValue("image", "error.product", "Vui lòng chọn ảnh sản phẩm!");
        }
        if (result.hasErrors()) {
            List<Product> products = productService.findAll();
            model.addAttribute("products", products);
            model.addAttribute("searchProduct", null);
            model.addAttribute("hideModal", "yes");
            return "product/list";
        }

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
        boolean check = productService.save(productNew);
        if( !check) {
            model.addAttribute("errorAdd", "Lỗi khi lưu sản phẩm!");
            return "product/list";
        }
        return "redirect:/products";
    }

    @GetMapping("products/delete")
    public String deleteCourse(@RequestParam("id") int id) {
        productService.delete(id);
        return "redirect:/products";
    }
}
