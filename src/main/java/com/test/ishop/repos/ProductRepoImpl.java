package com.test.ishop.repos;

import com.test.ishop.domain.Product;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class ProductRepoImpl implements ProductCustomRepo{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Product> findByNativeQuery(String sql) {
        return em.createNativeQuery(sql,Product.class).getResultList();
    }
}
