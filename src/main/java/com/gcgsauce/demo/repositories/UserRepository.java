package com.gcgsauce.demo.repositories;

import com.gcgsauce.demo.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>{

    User findByUsername(String name);
    void deleteByUsername(String name);
    User findByEmail(String email);
}
