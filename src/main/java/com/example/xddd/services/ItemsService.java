package com.example.xddd.services;

import com.example.xddd.entities.Cart;
import com.example.xddd.entities.Item;
import com.example.xddd.entities.Status;
import com.example.xddd.entities.Us3r;
import com.example.xddd.repositories.CartRepository;
import com.example.xddd.repositories.ItemsRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

    public ResponseEntity<?> purchaseItems(Us3r user) {

        List<Cart> carts = cartRepository.findCartsByOwnerLogin(user.getLogin());

        for (Cart cart : carts) {
            Optional<Item> query = repository.findById(cart.getItemId());

            if (query.isPresent()) {
                Item item = query.get();

                if (item.getNumber() >= cart.getItemNumber()) {
                    item.setNumber(item.getNumber() - cart.getItemNumber());
                    cart.setStatus("purchased");
                    cartRepository.save(cart);
                    repository.save(item);
                } else return ResponseEntity.ok("Can not process order");
            } else return ResponseEntity.ok("Can not process order");
        }

        return ResponseEntity.ok().build();


    }
}