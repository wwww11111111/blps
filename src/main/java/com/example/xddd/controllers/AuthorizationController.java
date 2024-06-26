package com.example.xddd.controllers;

import com.example.xddd.entities.User;
import com.example.xddd.repositories.RoleRepository;
import com.example.xddd.repositories.UserRepository;
import com.example.xddd.security.*;
import com.example.xddd.xmlrepo.UserRepositoryXmlImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "${cors.urls}")
@RestController
@RequestMapping("/api/auth")
public class AuthorizationController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRepositoryXmlImpl xmlrepo;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody ObjectNode json) {

        final var authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                json.get("login").asText(),
                                json.get("password").asText()));
        if (!authentication.isAuthenticated()) {
//            throw new UnauthorizedException("Bad credentials");
            throw new RuntimeException("Bad credentials");
        }
        final String token = jwtUtil.generateJwtToken(authentication);
//        final var user = userRepository.findByLogin(authentication.getName()).get();

        final var user = xmlrepo.findByLogin(authentication.getName());


        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        ArrayNode arrayNode = objectMapper.createArrayNode();

        objectNode.put("id", user.getId());
        objectNode.put("token", token);

        List<Role> roles = user.getRole();

        for (Role role : roles) {
            arrayNode.add(role.getName().name());
        }

        objectNode.set("roles", arrayNode);

        objectNode.put("expires", jwtUtil.getExpirationDate(token).toInstant().toString());

        return ResponseEntity.ok().body(objectNode);
    }


//    @PostMapping("/signup")
//    public ResponseEntity<?> registerUser(@RequestBody ObjectNode json) {
//
//        if (userRepository.existsByLogin(json.get("login").asText()))
//            throw new RuntimeException("Error: Phone is already taken!");
//
//        User user = new User(json.get("login").asText(),
//                encoder.encode(json.get("password").asText()));
//
//
//        User user1 = new User(json.get("login").asText(),
//                encoder.encode(json.get("password").asText()));
//        user1.getRole().add(roleRepository.findByName(ERole.ROLE_USER));
//
//        user.getRole().add(roleRepository.findByName(ERole.ROLE_USER));
//        System.out.println(user.getRole().getClass());
//        user = userRepository.save(user);
//        xmlrepo.save(user1);
//
//
//        return ResponseEntity.ok().body("User registered successfully!");
//    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser1(@RequestBody ObjectNode json) {


        User user = xmlrepo.findByLogin(json.get("login").asText());

        if (user != null) {
            throw new RuntimeException("Error: Phone is already taken!");
        }

        user = new User(json.get("login").asText(),
                encoder.encode(json.get("password").asText()));

        user.setId(xmlrepo.getNextId());
        user.getRole().add(new Role(ERole.ROLE_USER));

        xmlrepo.save(user);


        return ResponseEntity.ok().body("User registered successfully!");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody ObjectNode json) {
        refreshTokenService.deleteByUserId(json.get("user-id").asLong());
        return ResponseEntity.ok().body("Log out successful!");
    }
}
