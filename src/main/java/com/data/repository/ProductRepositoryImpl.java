package com.data.repository;

import com.data.entity.Customer;
import com.data.entity.Product;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository{
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Product> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Product order by id", Product.class).getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> findPage(int page, int size) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Product order by id", Product.class)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
        }
    }

    @Override
    public List<Product> findAllStatusTrue() {
        try(Session session = sessionFactory.openSession()) {
            return session.createQuery("from Product where status = true order by id", Product.class).getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Product.class, id);
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean save(Product product) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(product);
            session.getTransaction().commit();
            return true;
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Product product) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(product);
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
            Product product = session.get(Product.class, id);
            if (product != null) {
                session.beginTransaction();
                session.delete(product);
                session.getTransaction().commit();
                return true;
            }
            return false;

        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
    }

//    @Override
//    public List<Product> findByBrand(String name) {
//        try (Session session = sessionFactory.openSession()) {
//            return session.createQuery("from Product where brand like :name", Product.class)
//                    .setParameter("name", "%" + name + "%")
//                    .getResultList();
//        } catch (HibernateException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }


    @Override
    public boolean existsByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            String normalized = name.trim().toLowerCase().replaceAll("\\s+", " ");
            return session.createQuery("select count(c) from Product c where c.name = :name", Long.class)
                    .setParameter("name", normalized).uniqueResult() > 0;
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean existsByNameEdit(String name, int id) {
        try (Session session = sessionFactory.openSession()) {
            String normalized = name.trim().toLowerCase().replaceAll("\\s+", " ");
            return session.createQuery(
                            "select count(c) from Product c where lower(trim(c.name)) = :name and c.id != :id",
                            Long.class)
                    .setParameter("name", normalized)
                    .setParameter("id", id)
                    .uniqueResult() > 0;
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Product> findByBrand(String brand) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Product where lower(brand) like :brand", Product.class)
                    .setParameter("brand", "%" + brand.toLowerCase().trim() + "%")
                    .getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Product> findByStock(int stock) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Product where stock = :stock", Product.class)
                    .setParameter("stock", stock)
                    .getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Product> findByPriceRange(double min, double max) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Product where price between :min and :max", Product.class)
                    .setParameter("min", min)
                    .setParameter("max", max)
                    .getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
