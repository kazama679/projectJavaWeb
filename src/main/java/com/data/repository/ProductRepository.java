package com.data.repository;

import com.data.entity.Customer;
import com.data.entity.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> findAll();
    List<Product> findAllStatusTrue();
    Product findById(int id);
    boolean save(Product product);
    List<Product> findPage(int page, int size);
    boolean update(Product product);
    boolean delete(int id);
    List<Product> findByBrand(String name);
    boolean existsByName(String name);
    boolean existsByNameEdit(String name, int id);
    List<Product> findByStock(int stock);
    List<Product> findByPriceRange(double min, double max);
}
