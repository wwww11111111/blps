package com.example.xddd.security.jaas;

import com.example.xddd.xmlrepo.UserRepositoryXmlImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class UserRepositoryAuthorityGranter implements AuthorityGranter {

    private final UserRepositoryXmlImpl userRepository;

    @Override
    public Set<String> grant(Principal principal) {

        Set<String> stringRoles = new HashSet<>();

         userRepository.findByLogin(principal.getName()).getRole().forEach(r -> {
            stringRoles.add(r.getName().name());

        });

         return stringRoles;
    }
}