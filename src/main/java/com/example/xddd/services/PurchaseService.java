package com.example.xddd.services;

import com.example.xddd.entities.User;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PurchaseService {

    private final ItemsService itemsService;
    private final Us3rService us3rService;

    public PurchaseService(ItemsService itemsService, Us3rService us3rService) {
        this.itemsService = itemsService;
        this.us3rService = us3rService;
    }

    public ResponseEntity<?> process(ObjectNode json) {

        User user = new User(
                json.get("user").get("login").asText(),
                json.get("user").get("password").asText()
        );

        ResponseEntity<?> response = us3rService.validateUs3r(user);

        if (response.getStatusCodeValue() == 401) {
            return response;
        }

        response = itemsService.purchaseItems(json);

        return response;
    }

}
