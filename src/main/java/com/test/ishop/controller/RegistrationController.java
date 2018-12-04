package com.test.ishop.controller;

import com.test.ishop.domain.Client;
import com.test.ishop.domain.Role;
import com.test.ishop.repos.ClientRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

@Controller
public class RegistrationController {
    private static final Logger LOGGER = Logger.getLogger(RegistrationController.class);

    @Autowired
    private ClientRepo clientRepo;

    @GetMapping("/registration")
    public String registration(Model model) {
        LOGGER.debug("registration is called.");

        return "registration";
    }

    private boolean verifyData(
            String login,
            String password,
            Model model
    ) {
        boolean result = true;
        String errorMessage = "";
        if ((login.isEmpty())||(password.isEmpty())) {
            errorMessage ="Не заполнены все необходимые поля";
        } else {
            if (clientRepo.findByUsername(login) != null) {
                errorMessage = "Пользователь уже существует";
            }
        }
        if (!errorMessage.isEmpty()) {
            LOGGER.debug("registration error:" + errorMessage + "(" + login + ")");
            model.addAttribute("errormessage", errorMessage);
            result=false;
        }
        return result;
    }

    private void saveClient(
            String login,
            String password,
            String username,
            String usersurname,
            String userpatronymic,
            String address,
            String email
    ) {
        Client client = new Client();
        client.setActive(true);
        client.setUsername(login);
        client.setPassword(password);
        client.setFirstName(username);
        client.setSurname(usersurname);
        client.setPatronymic(userpatronymic);
        client.setAddress(address);
        client.setEmail(email);
        client.setRoles(Collections.singleton(Role.USER));
        clientRepo.save(client);
        LOGGER.debug("Client is created="+client);
    }

    @PostMapping("/register")
    public String register(

            @RequestParam String login,
            @RequestParam String password,
            @RequestParam String username,
            @RequestParam String usersurname,
            @RequestParam String userpatronymic,
            @RequestParam String address,
            @RequestParam String email,
            Model model
    ) {
        LOGGER.debug("register is called.");
        if (!verifyData(login,password,model)) {
            return "registration";
        }
        saveClient(login,password,username,usersurname,userpatronymic,address,email);
        return "redirect:/";
    }

}
