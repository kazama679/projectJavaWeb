package com.data.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProductDTO {
    private int id;
    @NotBlank(message = "Tên sản phẩm không được để trống!")
    private String name;
    @NotBlank(message = "Tên nhãn hàng không được để trống!")
    private String brand;
    @Min(value = 0, message = "Giá sản phẩm không được nhỏ hơn 0!")
    private double price;
    @Min(value = 1, message = "Số lượng sản phẩm phải lớn hơn 0!")
    private int stock;
    private MultipartFile image;
    private boolean status;
}
