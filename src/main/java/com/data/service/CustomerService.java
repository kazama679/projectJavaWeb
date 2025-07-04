package com.data.service;

import com.data.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> findAll();
    List<Customer> findPage(int page, int size);
    Customer findById(int id);
    boolean save(Customer customer);
    boolean update(Customer customer);
    boolean delete(int id);
    List<Customer> findByName(String name);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByEmailEdit(String email, int id);
    boolean existsByPhoneEdit(String phone, int id);
}
