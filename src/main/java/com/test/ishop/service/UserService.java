package com.test.ishop.service;

import com.test.ishop.domain.Client;
import com.test.ishop.domain.Role;
import com.test.ishop.repos.ClientRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    private final static Logger LOGGER = Logger.getLogger(UserService.class);

    @Autowired
    private ClientRepo clientRepo;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        LOGGER.debug("UserService username="+userName);
        Client client = clientRepo.findByUsername(userName);

        if (client==null) {
            LOGGER.debug("Client not found");
            throw new UsernameNotFoundException(userName);
        }
        return new UserPrincipal(client);
    }

}

