package com.example.xddd.services;


import com.example.xddd.entities.User;
import com.example.xddd.repositories.UserRepository;
import com.example.xddd.security.UserDetailsClass;
import com.example.xddd.xmlrepo.UserRepositoryInterface;
import com.example.xddd.xmlrepo.UserRepositoryXmlImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceClass implements UserDetailsService {
    @Autowired
    UserRepositoryXmlImpl xmlrepo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = xmlrepo.findByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }

        return UserDetailsClass.build(user);
    }
}
