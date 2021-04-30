package com.example.demo.repositories;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

// CrudRepository - Інтерфейс для спільних операцій CRUD з репозиторієм певного типу.
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("Select u FROM User u WHERE u.username = :username")
    User getUserByUsername(@Param("username") String username);
}
