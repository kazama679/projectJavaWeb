package com.data.service;

import com.data.entity.Product;
import com.data.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public boolean delete(int id) {
        return productRepository.delete(id);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findAllStatusTrue() {
        return productRepository.findAllStatusTrue();
    }

    @Override
    public Product findById(int id) {
        return productRepository.findById(id);
    }

    @Override
    public boolean save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public boolean update(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> findPage(int page, int size) {
        return productRepository.findPage(page, size);
    }

    @Override
    public List<Product> findByBrand(String name) {
        return productRepository.findByBrand(name);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameEdit(String name, int id) {
        return productRepository.existsByNameEdit(name, id);
    }

    @Override
    public List<Product> findByStock(int stock) {
        return productRepository.findByStock(stock);
    }

    @Override
    public List<Product> findByPriceRange(double min, double max) {
        return productRepository.findByPriceRange(min, max);
    }
}