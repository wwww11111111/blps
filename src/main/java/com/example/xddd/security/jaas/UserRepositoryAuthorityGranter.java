package com.example.xddd.security.jaas;

import com.example.xddd.entities.User;
import com.example.xddd.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.jaas.AuthorityGranter;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class UserRepositoryAuthorityGranter implements AuthorityGranter {

    private final UserRepository userRepository;

    @Override
    public Set<String> grant(Principal principal) {
//        final var role = userRepository.findRoleByLogin(principal.getName());
//        return role.map(value -> Set.of(value.toString())).orElse(null);

        Optional<User> user = userRepository.findById(principal.get)
    }
}