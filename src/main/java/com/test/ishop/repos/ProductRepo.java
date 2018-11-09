package com.test.ishop.repos;

import com.test.ishop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Long>,ProductCustomRepo {
    List<Product> findByCategoryId(Long id);
}
