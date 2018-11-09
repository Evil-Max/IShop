package com.test.ishop.repos;

import com.test.ishop.domain.Product;

import java.util.List;

public interface ProductCustomRepo {
    List<Product> findByNativeQuery(String sql);
}
