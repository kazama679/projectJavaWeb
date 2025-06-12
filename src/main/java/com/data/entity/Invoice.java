package com.data.entity;

import com.data.ulti.StatusInvoice;

import java.util.Date;

public class Invoice {
    private int id;
    private int customer_id;
    private Date created_at;
    private double total_amount;
    private StatusInvoice status; // default PENDING
}
