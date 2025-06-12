package com.data.repository;

import com.data.entity.Admin;

import java.util.List;

public interface AdminRepository {
    List<Admin> findAll();
}