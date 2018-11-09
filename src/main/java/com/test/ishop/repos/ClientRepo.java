package com.test.ishop.repos;

import com.test.ishop.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepo extends JpaRepository<Client,Long> {
    Client findByUsername(String userbane);
}

