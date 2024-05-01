package com.example.xddd.xmlrepo;

import com.example.xddd.entities.User;

public interface UserRepositoryInterface {
    User findByUsername(String username);
    void save(User user);
}