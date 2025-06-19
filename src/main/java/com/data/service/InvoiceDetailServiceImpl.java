package com.data.service;

import com.data.entity.InvoiceDetail;
import com.data.repository.InvoiceDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceDetailServiceImpl implements InvoiceDetailService {
    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;

    @Override
    public List<InvoiceDetail> findAll() {
        return invoiceDetailRepository.findAll();
    }

    @Override
    public List<InvoiceDetail> findAllByIdInvoice(int id) {
        return invoiceDetailRepository.findAllByIdInvoice(id);
    }

    @Override
    public boolean save(InvoiceDetail invoiceDetail) {
        return invoiceDetailRepository.save(invoiceDetail);
    }
}
