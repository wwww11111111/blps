package com.example.xddd.repositories;

import com.example.xddd.entities.Us3r;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Us3rRepository extends CrudRepository<Us3r, Long> {

    Us3r findByLogin(String Login);
}
