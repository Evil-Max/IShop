package com.test.ishop.repos;

import com.test.ishop.domain.Order;

import java.util.List;

public interface OrderCustomRepo {
    List<Order> findByNativeQuery(String sql);
}
