package com.data.repository;

import com.data.entity.Customer;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerRepositoryImpl  implements CustomerRepository{
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Customer> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Customer order by id", Customer.class).getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }
}
