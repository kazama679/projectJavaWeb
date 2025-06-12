package com.data.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProductDTO {
    private int id;
//    @NotBlank(message = "Tên sản phẩm không được để trống!")
    private String name;
//    @NotBlank(message = "Tên nhãn hàng không được để trống!")
    private String brand;
//    @NotBlank(message = "Giá sản phẩm không được để trống!")
    private double price;
//    @NotBlank(message = "Số lượng sản phẩm không được để trống!")
    private int stock;
//    @NotBlank(message = "Ảnh sản phẩm không được để trống!")
    private String image;
    private boolean status;
}
