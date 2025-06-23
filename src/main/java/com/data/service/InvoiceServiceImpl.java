package com.data.service;

import com.data.entity.Invoice;
import com.data.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public Invoice findById(int id) {
        return invoiceRepository.findById(id);
    }

    @Override
    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    @Override
    public boolean save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    public List<Invoice> findPage(int page, int size) {
        return invoiceRepository.findPage(page, size);
    }

    @Override
    public boolean update(Invoice invoice) {
        return invoiceRepository.update(invoice);
    }

    @Override
    public List<Invoice> searchByCustomerName(String name) {
        return invoiceRepository.searchByCustomerName(name);
    }

    @Override
    public List<Invoice> searchByCreatedDate(Date date) {
        return invoiceRepository.searchByCreatedDate(date);
    }
}
