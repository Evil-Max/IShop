package com.test.ishop.repos;

import com.test.ishop.domain.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAttributeRepo extends JpaRepository<ProductAttribute, Long> {
}
