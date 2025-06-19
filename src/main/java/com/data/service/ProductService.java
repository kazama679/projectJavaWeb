package com.data.service;

import com.data.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(int id);
    boolean save(Product product);
    boolean update(Product product);
    boolean delete(int id);
    List<Product> findByBrand(String name);
    boolean existsByName(String name);
    boolean existsByNameEdit(String name, int id);
    List<Product> findByStock(int stock);
    List<Product> findByPriceRange(double min, double max);
}
