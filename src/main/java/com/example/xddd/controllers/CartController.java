package com.example.xddd.controllers;

import com.example.xddd.entities.Item;
import com.example.xddd.services.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @RequestMapping("/add")
    public ResponseEntity<?> add(@RequestBody ObjectNode json) {

        return service.add(json);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody ObjectNode json) {
        return service.delete(json);
    }

    @RequestMapping("/getCart")
    public ResponseEntity<?> getCart(@RequestBody ObjectNode json) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        ObjectNode root = objectMapper.createObjectNode();
//
//
//        Item item = new Item(1L, 2, 3);
//        Item item2 = new Item(1L, 2, 3);
//
//
//        JsonNode node = objectMapper.valueToTree(item2);
//
//        JsonNode node2 = objectMapper.valueToTree(item);
//
//        ArrayNode array = objectMapper.createArrayNode().add(node2).add(node);
//
//
//        root.set("items", array);




        return service.getCart(json);
    }
}
