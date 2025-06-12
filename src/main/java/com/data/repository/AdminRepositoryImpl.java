package com.data.repository;

import com.data.entity.Admin;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminRepositoryImpl implements AdminRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Admin> findAll() {
        try(Session session = sessionFactory.openSession()) {
            return session.createQuery("from Admin", Admin.class).getResultList();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
