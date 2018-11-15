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
import java.util.Map;

@Controller
public class RegistrationController {
    private final static Logger LOGGER = Logger.getLogger(RegistrationController.class);

    @Autowired
    private ClientRepo clientRepo;

    @GetMapping("/registration")
    public String registration(Model model) {
        LOGGER.debug("registration is called.");

        return "registration";
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

            @RequestParam Map<String, String> form,

            Model model
    ) {
        LOGGER.debug("register is called.");
        String errorMessage = "";
        if ((login.isEmpty())||(password.isEmpty())) {
            errorMessage ="Не заполнены все поля";
            //fillModel(model);
            model.addAttribute("errormessage", errorMessage);
            LOGGER.info("registration error:"+errorMessage+"(login="+login+", password="+password+")");
            return "registration";
        }
        Client client;
        client = clientRepo.findByUsername(login);
        if (client!=null) {
            errorMessage ="Пользователь уже существует";

            model.addAttribute("errormessage", errorMessage);
            LOGGER.info("registration error:"+errorMessage+"("+login+")");
            return "registration";
        }
        client = new Client();
        client.setActive(true);
        client.setUsername(login);
        client.setPassword(password);

        client.setFirst_name(username);
        client.setSurname(usersurname);
        client.setPatronymic(userpatronymic);
        client.setAddress(address);
        client.setEmail(email);
        client.setRoles(Collections.singleton(Role.USER));
        clientRepo.save(client);
        LOGGER.debug("Client is created="+client);

        return "redirect:/";
    }

}
