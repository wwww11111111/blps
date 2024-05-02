package com.example.xddd.xmlrepo;

import com.example.xddd.entities.User;

public interface UserRepositoryInterface {
    User findByLogin(String username);
    void save(User user);
}