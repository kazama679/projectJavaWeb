package com.data.service;

import com.data.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    List<Product> findAllStatusTrue();
    Product findById(int id);
    boolean save(Product product);
    boolean update(Product product);
    List<Product> findPage(int page, int size);
    boolean delete(int id);
    List<Product> findByBrand(String name);
    boolean existsByName(String name);
    boolean existsByNameEdit(String name, int id);
    List<Product> findByStock(int stock);
    List<Product> findByPriceRange(double min, double max);
}
