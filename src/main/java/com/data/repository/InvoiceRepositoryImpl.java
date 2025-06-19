package com.data.repository;

import com.data.entity.Customer;
import com.data.entity.Invoice;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class InvoiceRepositoryImpl implements InvoiceRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Invoice findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Invoice.class, id);
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Invoice> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Invoice order by id", Invoice.class).getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean save(Invoice invoice) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(invoice);
            session.getTransaction().commit();
            return true;
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Invoice invoice) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(invoice);
            session.getTransaction().commit();
            return true;
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Invoice> searchByCustomerName(String name) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "from Invoice i where i.customer.name like :name order by i.id", Invoice.class)
                    .setParameter("name", "%" + name + "%")
                    .getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Invoice> searchByCreatedDate(Date date) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "from Invoice where date(created_at) = :date", Invoice.class)
                    .setParameter("date", date)
                    .getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }
}
