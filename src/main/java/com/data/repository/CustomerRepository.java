package com.data.repository;

import com.data.entity.Customer;

import java.util.List;

public interface CustomerRepository {
    List<Customer> findAll();
}
