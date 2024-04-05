package com.example.xddd.repositories;

import com.example.xddd.entities.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long> {


    Cart findByOwnerLoginAndItemId(String ownerLogin, long itemId);

    List<Cart> findCartsByOwnerLogin(String ownerLogin);
}
