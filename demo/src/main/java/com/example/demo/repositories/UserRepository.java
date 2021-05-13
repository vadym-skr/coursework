package com.example.demo.repositories;

import com.example.demo.entity.User;
import jdk.dynalink.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

// CrudRepository - Інтерфейс для спільних операцій CRUD з репозиторієм певного типу.
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("Select u FROM User u WHERE u.username = :username")
    User findUserByUsername(@Param("username") String username);

    @Query("Select u FROM User u WHERE u.id = :id")
    User findUserById(@Param("id") Integer id);

    @Query("Select u FROM User u WHERE u.username like %:username%")
    List<User> findUsersByUsername(@Param("username") String username);

    @Query("Select u FROM User u " +
            "WHERE u.username like %:username% " +
            "AND u.email like %:email% " +
            "AND (u.enabled = :enabledOne OR u.enabled = :enabledTwo) ")
    List<User> findUsersByAllFields(@Param("username") String username,
                                    @Param("email") String email,
                                    @Param("enabledOne") Boolean enabledOne,
                                    @Param("enabledTwo") Boolean enabledTwo);

    boolean existsUserByUsername(String username);

    long count();
}
