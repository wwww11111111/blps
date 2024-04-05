package com.example.xddd.controllers;

import com.example.xddd.services.ItemsService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemsController {

    private final ItemsService service;

    public ItemsController(ItemsService service) {
        this.service = service;
    }

    @RequestMapping("/items")
    public ResponseEntity<?> items(@RequestParam(required = false) Integer categoryId) {
        return service.getItems(categoryId);
    }
}
