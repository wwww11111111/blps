package com.example.xddd.services;

import com.example.xddd.entities.Us3r;
import com.example.xddd.repositories.Us3rRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class Us3rService {

    private final Us3rRepository repository;

    public Us3rService(Us3rRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<?> validateUs3r(Us3r user) {

        String password = user.getPassword();

        user = repository.findByLogin(user.getLogin());

        if (user == null) {
            return ResponseEntity.status(401).body("No such user exists");
        }

        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(401).body("Incorrect password");
        }

        return ResponseEntity.ok().build();
    }
}
