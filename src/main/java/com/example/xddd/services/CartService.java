package com.example.xddd.services;

import com.example.xddd.entities.Cart;
import com.example.xddd.entities.Status;
import com.example.xddd.entities.Us3r;
import com.example.xddd.repositories.CartRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final ItemsService itemsService;
    private final Us3rService us3rService;
    private final CartRepository repository;

    public CartService(ItemsService itemsService, Us3rService us3rService, CartRepository repository) {
        this.itemsService = itemsService;
        this.us3rService = us3rService;
        this.repository = repository;
    }


    public ResponseEntity<?> add(ObjectNode json) {

        Us3r user = new Us3r(
                json.get("user").get("login").asText(),
                json.get("user").get("password").asText()
        );

        ResponseEntity<?> response = us3rService.validateUs3r(user);

        if (response.getStatusCodeValue() == 401) {
            return response;
        }

        int number = Integer.parseInt(
                json.get("number").asText()
        );

        int id = Integer.parseInt(
                json.get("id").asText()
        );

        Cart cart = repository.findByOwnerLoginAndItemId(
                user.getLogin(), id
        );

        if (cart == null) {
            repository.save(new Cart(user.getLogin(),
                    id, number, "reserved"));
        } else {
            cart.setItemNumber(cart.getItemNumber() + number);
            repository.save(cart);
        }

        return ResponseEntity.ok().build();
    }


    public ResponseEntity<?> delete(ObjectNode json) {
        Us3r user = new Us3r(
                json.get("user").get("login").asText(),
                json.get("user").get("password").asText()
        );

        ResponseEntity<?> response = us3rService.validateUs3r(user);

        if (response.getStatusCodeValue() == 401) {
            return response;
        }

        int id = Integer.parseInt(
                json.get("id").asText()
        );

        Cart cart = repository.findByOwnerLoginAndItemId(
                user.getLogin(), id
        );

        if (cart != null) {
            repository.delete(cart);
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> getCart(ObjectNode json) {
        Us3r user = new Us3r(
                json.get("user").get("login").asText(),
                json.get("user").get("password").asText()
        );

        ResponseEntity<?> response = us3rService.validateUs3r(user);

        if (response.getStatusCodeValue() == 401) {
            return response;
        }

        List<Cart> items = repository.findCartsByOwnerLogin(user.getLogin());

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = objectMapper.createObjectNode();

        ArrayNode array = objectMapper.createArrayNode();

        for (Cart item : items) {
            array.add(objectMapper.valueToTree(item));
        }


        root.set("items", array);
        return ResponseEntity.ok().body(root);
    }


}
