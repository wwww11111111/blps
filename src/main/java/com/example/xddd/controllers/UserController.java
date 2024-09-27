package com.example.xddd.controllers;

import com.example.xddd.entities.User;
import com.example.xddd.xmlrepo.UserRepositoryXmlImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserRepositoryXmlImpl repositoryXml;

    @GetMapping("/balance")
    public ResponseEntity<?> getBalance() {

        User user = repositoryXml.findByLogin(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        return ResponseEntity.ok().body("{\"balance\": \"" + user.getBalance() + "\"}");

    }
}
