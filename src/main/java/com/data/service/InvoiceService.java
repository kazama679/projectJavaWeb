package com.data.service;

import com.data.entity.Invoice;

import java.util.Date;
import java.util.List;

public interface InvoiceService {
    Invoice findById(int id);
    List<Invoice> findAll();
    boolean save(Invoice invoice);
    List<Invoice> findPage(int page, int size);
    boolean update(Invoice invoice);
    List<Invoice> searchByCustomerName(String name);
    List<Invoice> searchByCreatedDate(Date date);
}
