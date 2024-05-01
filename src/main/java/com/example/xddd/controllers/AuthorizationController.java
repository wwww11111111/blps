package com.example.xddd.controllers;

import com.example.xddd.entities.RefreshToken;
import com.example.xddd.entities.User;
import com.example.xddd.repositories.RoleRepository;
import com.example.xddd.repositories.UserRepository;
import com.example.xddd.security.*;
import com.example.xddd.xmlrepo.UserRepositoryXmlImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.XStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        final var user = userRepository.findByLogin(authentication.getName()).get();


        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        ArrayNode arrayNode = objectMapper.createArrayNode();

        objectNode.put("id", user.getId());
        objectNode.put("token", token);

        List<Role> roles = user.getRoles();

        for (Role role : roles) {
            arrayNode.add(role.getName().name());
        }

        objectNode.set("roles", arrayNode);

        objectNode.put("expires", jwtUtil.getExpirationDate(token).toInstant().toString());

        return ResponseEntity.ok().body(objectNode);
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody ObjectNode json) {

        if (userRepository.existsByLogin(json.get("login").asText()))
            throw new RuntimeException("Error: Phone is already taken!");

        User user = new User(json.get("login").asText(),
                encoder.encode(json.get("password").asText()));

        user.getRoles().add(roleRepository.findByName(ERole.ROLE_USER));
        userRepository.save(user);
        xmlrepo.save(user);

        XStream xStream = new XStream();

        System.out.println(xStream.toXML(user.getRoles()));


        return ResponseEntity.ok().body("User registered successfully!");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody ObjectNode json) {
        refreshTokenService.deleteByUserId(json.get("user-id").asLong());
        return ResponseEntity.ok().body("Log out successful!");
    }
}
