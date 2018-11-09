package com.test.ishop.repos;

import com.test.ishop.domain.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeRepo extends JpaRepository<Attribute,Long> {
}
