package com.example.xddd.controllers;

import com.example.xddd.entities.Us3r;
import com.example.xddd.services.Us3rService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Us3rController {

    private final Us3rService service;

    public Us3rController(Us3rService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Us3r user) {
        return service.validateUs3r(user);
    }
}
