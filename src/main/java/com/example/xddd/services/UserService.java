package com.example.xddd.services;

import com.example.xddd.entities.User;
import com.example.xddd.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<?> validateUs3r(User user) {



        String password = user.getPassword();

        user = repository.findByLogin(user.getLogin()).get();

        if (user == null) {
            return ResponseEntity.status(401).body("No such user exists");
        }

        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(401).body("Incorrect password");
        }

        return ResponseEntity.ok().build();
    }
}
