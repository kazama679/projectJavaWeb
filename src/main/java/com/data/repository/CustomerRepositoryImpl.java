package com.data.repository;

import com.data.entity.Customer;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository{
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

    @Override
    public Customer findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Customer.class, id);
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean save(Customer customer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(customer);
            session.getTransaction().commit();
            return true;
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Customer customer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(customer);
            session.getTransaction().commit();
            return true;
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            Customer customer = session.get(Customer.class, id);
            if (customer != null) {
                session.beginTransaction();
                session.delete(customer);
                session.getTransaction().commit();
                return true;
            }
            return false;
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Customer> findByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Customer where name like :name", Customer.class)
                    .setParameter("name", "%" + name + "%")
                    .getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            String normalized = email.trim().toLowerCase().replaceAll("\\s+", " ");
            return session.createQuery("select count(c) from Customer c where c.email = :email", Long.class)
                    .setParameter("email", normalized).uniqueResult() > 0;
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean existsByPhone(String phone) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select count(c) from Customer c where c.phone = :phone", Long.class)
                    .setParameter("phone", phone).uniqueResult() > 0;
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean existsByPhoneEdit(String phone, int id) {
        try (Session session = sessionFactory.openSession()) {
            String normalized = phone.trim().toLowerCase().replaceAll("\\s+", " ");
            return session.createQuery(
                            "select count(c) from Customer c where lower(trim(c.phone)) = :phone and c.id != :id",
                            Long.class)
                    .setParameter("phone", normalized)
                    .setParameter("id", id)
                    .uniqueResult() > 0;
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean existsByEmailEdit(String email, int id) {
        try (Session session = sessionFactory.openSession()) {
            String normalized = email.trim().toLowerCase().replaceAll("\\s+", " ");
            return session.createQuery(
                            "select count(c) from Customer c where lower(trim(c.email)) = :email and c.id != :id",
                            Long.class)
                    .setParameter("email", normalized)
                    .setParameter("id", id)
                    .uniqueResult() > 0;
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
    }
}
