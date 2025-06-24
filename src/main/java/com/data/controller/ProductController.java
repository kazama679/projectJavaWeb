package com.data.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.data.dto.ProductDTO;
import com.data.entity.Customer;
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
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "stock", required = false) Integer stock,
            @RequestParam(value = "min", required = false) Double min,
            @RequestParam(value = "max", required = false) Double max,
            HttpSession session,
            Model model
    ) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }
        List<Product> filteredProducts;

        if (brand != null && !brand.trim().isEmpty()) {
            filteredProducts = productService.findByBrand(brand);
        } else if (stock != null) {
            filteredProducts = productService.findByStock(stock);
        } else if (min != null && max != null) {
            filteredProducts = productService.findByPriceRange(min, max);
        } else {
            filteredProducts = productService.findAll();
        }

        int totalProducts = filteredProducts.size();
        int totalPages = (int) Math.ceil((double) totalProducts / size);
        if (totalPages == 0) totalPages = 1;

        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        int start = (page - 1) * size;
        int end = Math.min(start + size, totalProducts);
        List<Product> paginatedProducts = filteredProducts.subList(start, end);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("products", paginatedProducts);
        model.addAttribute("productDTO", new ProductDTO());

        model.addAttribute("brand", brand);
        model.addAttribute("stock", stock);
        model.addAttribute("min", min);
        model.addAttribute("max", max);
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

//    @GetMapping("products/delete")
//    public String deleteCourse(@RequestParam("id") int id) {
//        productService.delete(id);
//        return "redirect:/products";
//    }

    @GetMapping("/products/updateStatus")
    public String updateStatus(int id) {
        Product product = productService.findById(id);
        if (product != null) {
            product.setStatus(!product.isStatus());
            productService.save(product);
        }
        return "redirect:/products";
    }

    @GetMapping("/products/edit")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/products";
        }

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setBrand(product.getBrand());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setStatus(product.isStatus());

        model.addAttribute("productDTO", dto);
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        model.addAttribute("oldImage", product.getImage()); // truyền ảnh cũ qua view (không trong DTO)

        return "product/edit"; // file html
    }

    @PostMapping("/products/update")
    public String updateProduct(@Valid @ModelAttribute("productDTO") ProductDTO dto,
                                BindingResult result,
                                @RequestParam("oldImage") String oldImage,
                                Model model) {
        if (productService.existsByNameEdit(dto.getName(), dto.getId())) {
            result.rejectValue("name", "error.product", "Tên sản phẩm đã tồn tại!");
        }
        if (result.hasErrors()) {
            model.addAttribute("oldImage", oldImage);
            return "product/edit";
        }
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setStatus(dto.isStatus());

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            try {
                Map upload = cloudinary.uploader().upload(dto.getImage().getBytes(), ObjectUtils.emptyMap());
                String imageUrl = (String) upload.get("url");
                product.setImage(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Lỗi upload ảnh: " + e.getMessage());
            }
        } else {
            product.setImage(oldImage);
        }
        boolean updated = productService.update(product);
        if (!updated) {
            model.addAttribute("errorUpdate", "Cập nhật thất bại!");
            model.addAttribute("oldImage", oldImage);
            return "product/edit";
        }
        return "redirect:/products";
    }
}
