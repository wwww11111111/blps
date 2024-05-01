package com.example.xddd.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {


    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/hello")
    public String hello() {
        return "Hello world";
    }
}
