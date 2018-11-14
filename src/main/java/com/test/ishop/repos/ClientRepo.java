package com.test.ishop.repos;

import com.test.ishop.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface ClientRepo extends JpaRepository<Client,Long> {
    Client findByUsername(String username);
    //@Query("select c from client c where c.username = ?1")
    //Client findByUsername(String username);
}

