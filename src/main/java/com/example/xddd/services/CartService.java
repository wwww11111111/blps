package com.example.xddd.services;

import com.example.xddd.entities.Cart;
import com.example.xddd.entities.Us3r;
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

        Cart cart = repository.findByOwnerLoginAndItemIdAndStatus(
                user.getLogin(), id, "reserved"
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

        Cart cart = repository.findByOwnerLoginAndItemIdAndStatus(
                user.getLogin(), id, "reserved"
        );

        if (cart != null) {
            repository.delete(cart);
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> getCart(ObjectNode json) {
        Us3r user;

        try {

            user = new Us3r(
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

            array.add(((ObjectNode)objectMapper.valueToTree(item))
                    .put("description",
                            description));
        }



        root.set("items", array);
        return ResponseEntity.ok().body(root);
    }


}
