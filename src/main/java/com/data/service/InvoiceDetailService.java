package com.data.service;

import com.data.entity.InvoiceDetail;

import java.util.List;

public interface InvoiceDetailService {
    List<InvoiceDetail> findAll();
    List<InvoiceDetail> findAllByIdInvoice(int id);
    boolean save(InvoiceDetail invoiceDetail);
}
