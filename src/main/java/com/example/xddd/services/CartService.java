package com.example.xddd.services;

import com.example.xddd.entities.Cart;
import com.example.xddd.entities.User;
import com.example.xddd.repositories.CartRepository;
import com.example.xddd.repositories.ItemsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final ItemsRepository itemsRepository;
    private final Us3rService us3rService;
    private final CartRepository repository;

    public CartService(ItemsRepository itemsRepository, Us3rService us3rService, CartRepository repository) {
        this.itemsRepository = itemsRepository;
        this.us3rService = us3rService;
        this.repository = repository;
    }


    public ResponseEntity<?> add(ObjectNode json) {

        User user = new User(
                json.get("user").get("login").asText(),
                json.get("user").get("password").asText()
        );

        ResponseEntity<?> response = us3rService.validateUs3r(user);

        if (response.getStatusCodeValue() == 401) {
            return response;
        }

        int number;
        try {
            number = Integer.parseInt(
                    json.get("number").asText()
            );
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Missing some required parameters");
        }

        int id;

        try {
            id = Integer.parseInt(
                    json.get("id").asText()
            );
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Missing some required parameters");

        }

        Cart cart = repository.findByOwnerLoginAndItemIdAndStatus(
                user.getLogin(), id, "reserved"
        );

        if (cart == null) {

            cart = new Cart(user.getLogin(),
                    id, number, "reserved", null);
            repository.save(cart);
        } else {
            cart.setItemNumber(cart.getItemNumber() + number);
            repository.save(cart);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode objectNode = objectMapper.valueToTree(cart);

        objectNode.remove("orderId");

        return ResponseEntity.ok().body(objectNode);
    }


    public ResponseEntity<?> delete(ObjectNode json) {
        User user = new User(
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

        Cart cart = repository.findByOwnerLoginAndItemIdAndStatus(
                user.getLogin(), id, "reserved"
        );

        if (cart != null) {
            repository.delete(cart);
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> getCart(ObjectNode json) {
        User user;

        try {

            user = new User(
                    json.get("user").get("login").asText(),
                    json.get("user").get("password").asText()
            );
        } catch (NullPointerException e) {
            return ResponseEntity.status(401).body("incorrect data");
        }


        ResponseEntity<?> response = us3rService.validateUs3r(user);

        if (response.getStatusCodeValue() == 401) {
            return response;
        }

        List<Cart> items = repository.findCartsByOwnerLoginAndStatus(user.getLogin(), "reserved");

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = objectMapper.createObjectNode();

        ArrayNode array = objectMapper.createArrayNode();

        for (Cart item : items) {

            String description = itemsRepository.findById(item.getItemId()).get().getDescription();

            ObjectNode newNode;

            newNode = ((ObjectNode)objectMapper.valueToTree(item)).put("description",
                    description);

            newNode.remove("orderId");

            array.add(newNode);
        }



        root.set("items", array);
        return ResponseEntity.ok().body(root);
    }


}
