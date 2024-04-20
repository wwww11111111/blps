package com.example.xddd.services;

import com.example.xddd.entities.Cart;
import com.example.xddd.entities.Item;
import com.example.xddd.repositories.CartRepository;
import com.example.xddd.repositories.ItemsRepository;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemsService {

    private final ItemsRepository repository;
    private final CartRepository cartRepository;

    public ItemsService(ItemsRepository repository, CartRepository cartRepository) {
        this.repository = repository;
        this.cartRepository = cartRepository;
    }

    public ResponseEntity<?> getItems(Integer categoryId) {

        List<Item> items = new ArrayList<>();

        if (categoryId != null) {
            items.addAll(repository.findByCategoryId(categoryId));
        } else {
            repository.findAll().forEach(items::add);
        }
        return ResponseEntity.ok().body(items);
    }

    public ResponseEntity<?> purchaseItems(ObjectNode json) {

        if (!validateOrderDetails(json)) {
            return ResponseEntity.ok("Can not process order due to incorrect details");
        }

        String login = json.get("user").get("login").asText();

        List<Cart> carts = cartRepository
                .findCartsByOwnerLoginAndStatus(login, "reserved");

        for (Cart cart : carts) {
            Optional<Item> query = repository.findById(cart.getItemId());
            Cart alreadyPurchased = cartRepository
                    .findByOwnerLoginAndItemIdAndStatus(login, cart.getItemId(), "purchased");

            if (query.isPresent()) {
                Item item = query.get();

                if (item.getNumber() >= cart.getItemNumber()) {
                    item.setNumber(item.getNumber() - cart.getItemNumber());

                    if (alreadyPurchased != null) {
                        alreadyPurchased.setItemNumber(alreadyPurchased.getItemNumber() + cart.getItemNumber());
                    } else cart.setStatus("purchased");
                    cartRepository.save(cart);
                    repository.save(item);
                } else return ResponseEntity.ok("Can not process order");
            } else return ResponseEntity.ok("Can not process order");
        }

        return ResponseEntity.ok().build();


    }

    private boolean validateOrderDetails(ObjectNode json) {

        try {
            String textData = json.get("delivery_date").asText();
            String address = json.get("address").asText();
            String paymentType = json.get("payment_type").asText();
            LocalDate date = LocalDate.parse(textData);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}