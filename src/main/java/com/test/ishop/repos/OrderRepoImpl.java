package com.test.ishop.repos;

import com.test.ishop.domain.Order;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class OrderRepoImpl implements OrderCustomRepo{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Order> findByNativeQuery(String sql) {
        return em.createNativeQuery(sql,Order.class).getResultList();
    }
}

