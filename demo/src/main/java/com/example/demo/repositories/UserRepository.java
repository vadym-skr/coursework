package com.example.demo.repositories;

import com.example.demo.entity.User;
import jdk.dynalink.Operation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

// CrudRepository - Інтерфейс для спільних операцій CRUD з репозиторієм певного типу.
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("Select u FROM User u WHERE u.username = :username")
    User findUserByUsername(@Param("username") String username);

    @Query("Select u FROM User u WHERE u.id = :id")
    User findUserById(@Param("id") Integer id);

    List<User> findUsersByUsername(String username);

    boolean existsUserByUsername(String username);
}
