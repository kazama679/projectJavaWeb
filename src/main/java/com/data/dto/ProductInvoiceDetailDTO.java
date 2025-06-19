package com.data.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
public class ProductInvoiceDetailDTO {
    private int id;
    private String name;
    private String brand;
    private double price;
    private int stock;
    private String image;
    private boolean status;

    private boolean isExistInvoice;
    private int quantity;
}
