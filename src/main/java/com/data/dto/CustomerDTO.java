package com.data.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CustomerDTO {
    private int id;
    @NotBlank(message = "Tên người dùng không được để trống!")
    private String name;
    @NotBlank(message = "Số điện thoại không được để trống!")
    @Pattern(regexp = "^(0[3|5|7|8|9])+([0-9]{8})$", message = "Không đúng định dạng số điện thoại!")
    private String phone;
    @NotBlank(message = "Email không được để trống!")
    @Email(message = "Email khônh đúng định dạng")
    private String email;
    @NotBlank(message = "Địa chỉ không được để trống!")
    private String address;
    private boolean status;
}
