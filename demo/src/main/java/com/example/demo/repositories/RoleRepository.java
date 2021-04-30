package com.example.demo.repositories;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

// CrudRepository - Інтерфейс для спільних операцій CRUD з репозиторієм певного типу.
public interface RoleRepository extends CrudRepository<Role, Integer> {

    @Query("Select r FROM Role r WHERE r.name = :name")
    Role getRoleByName(@Param("name") String name);
}
