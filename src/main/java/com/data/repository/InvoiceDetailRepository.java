package com.data.repository;

import com.data.entity.InvoiceDetail;

import java.util.List;

public interface InvoiceDetailRepository {
    List<InvoiceDetail> findAll();
    List<InvoiceDetail> findAllByIdInvoice(int id);
    boolean save(InvoiceDetail invoiceDetail);
}
