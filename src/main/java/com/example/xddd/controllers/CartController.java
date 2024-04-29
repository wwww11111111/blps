package com.example.xddd.controllers;

import com.example.xddd.services.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    public ResponseEntity<?> getCart(@RequestBody(required = false) ObjectNode json) throws JsonProcessingException {


        return service.getCart(json);
    }
}
