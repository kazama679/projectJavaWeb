package com.data.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CustomerDTO {
    private int id;
    @NotBlank(message = "Tên người dùng không được để trống!")
    private String name;
    @NotBlank(message = "Số điện thoại không được để trống!")
    private String phone;
    @NotBlank(message = "Email không được để trống!")
    private String email;
    @NotBlank(message = "Địa chỉ không được để trống!")
    private String address;
    private boolean status;
}
