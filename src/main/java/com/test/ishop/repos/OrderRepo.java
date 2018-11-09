package com.test.ishop.repos;

import com.test.ishop.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long>, OrderCustomRepo {

}
