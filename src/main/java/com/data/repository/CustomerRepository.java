package com.data.repository;

import com.data.entity.Customer;
import java.util.List;

public interface CustomerRepository {
    List<Customer> findAll();
    Customer findById(int id);
    boolean save(Customer customer);
    boolean update(Customer customer);
    boolean delete(int id);
    List<Customer> findByName(String name);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
