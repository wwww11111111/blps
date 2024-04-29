package com.example.xddd.controllers;

import com.example.xddd.entities.RefreshToken;
import com.example.xddd.entities.User;
import com.example.xddd.repositories.RoleRepository;
import com.example.xddd.repositories.UserRepository;
import com.example.xddd.security.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody ObjectNode json) {


        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(json.get("login").asText(),
                        json.get("password").asText()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsClass userDetails = (UserDetailsClass) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .toList();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("token", jwt);
        objectNode.put("refresh-token", refreshToken.getToken());
        objectNode.put("id", userDetails.getId());
        objectNode.put("username", userDetails.getUsername());

//        System.out.println(userRepository.findRolesById(userDetails.getId()));


        ArrayNode arrayNode = objectMapper.createArrayNode();

        for (String role : roles) {
            arrayNode.add(role);
        }

        objectNode.set("roles", arrayNode);

//        return ResponseEntity.ok(new JwtResponseDto(jwt, refreshToken.getToken(), userDetails.getId(),
//                userDetails.getUsername(), roles));

        return ResponseEntity.ok().body(objectNode);

    }

    @PostMapping("/newsignin")
    public ResponseEntity<?> newAuthenticateUser(@RequestBody ObjectNode json) {
        final var authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                json.get("login").asText(), json.get("password").asText()));
        if (!authentication.isAuthenticated()) {
            throw new RuntimeException("Bad credentials");
        }

        UserDetailsClass userDetails = (UserDetailsClass) authentication.getPrincipal();

        final String token = jwtUtils.generateJwtToken(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("token", token);
        objectNode.put("refresh-token", refreshToken.getToken());
        objectNode.put("id", userDetails.getId());
        objectNode.put("username", userDetails.getUsername());


        ArrayNode arrayNode = objectMapper.createArrayNode();

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .toList();

        for (String role : roles) {
            arrayNode.add(role);
        }

        objectNode.set("roles", arrayNode);

        return ResponseEntity.ok().body(objectNode);


    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@RequestBody ObjectNode json) {
        String requestRefreshToken = json.get("refresh-token").asText();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getLogin());

                    ObjectMapper objectMapper = new ObjectMapper();
                    ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("access-token", token);
                    objectNode.put("refresh-token", requestRefreshToken);
                    return ResponseEntity.ok().body(objectNode);
//                    return ResponseEntity.ok(new TokenRefreshResponseDto(token, requestRefreshToken));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody ObjectNode json) {

        if (userRepository.existsByLogin(json.get("login").asText()))
            throw new RuntimeException("Error: Phone is already taken!");

        User user = new User(json.get("login").asText(),
                encoder.encode(json.get("password").asText()));
        Set<Role> roles = new HashSet<>();

        roles.add(roleRepository.findByName(ERole.ROLE_USER));

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok().body("User registered successfully!");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody ObjectNode json) {
        refreshTokenService.deleteByUserId(json.get("user-id").asLong());
        return ResponseEntity.ok().body("Log out successful!");
    }
}
