package com.data.repository;

import com.data.entity.Invoice;
import com.data.entity.InvoiceDetail;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InvoiceDetailRepositoryImpl implements InvoiceDetailRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<InvoiceDetail> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from InvoiceDetail order by id", InvoiceDetail.class).getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<InvoiceDetail> findAllByIdInvoice(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from InvoiceDetail where invoice.id = :id order by id", InvoiceDetail.class)
                    .setParameter("id", id)
                    .getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean save(InvoiceDetail invoiceDetail) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(invoiceDetail);
            session.getTransaction().commit();
            return true;
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
    }
}
