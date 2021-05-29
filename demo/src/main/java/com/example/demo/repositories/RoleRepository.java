package com.example.demo.repositories;

import com.example.demo.entity.user.Role;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

// CrudRepository - Інтерфейс для спільних операцій CRUD з репозиторієм певного типу.
public interface RoleRepository extends CrudRepository<Role, Integer> {

    List<Role> findAll();

    @Query("Select r FROM Role r WHERE r.name = :name")
    Role getRoleByName(@Param("name") String name);

    @Modifying
    @Query(value = "DELETE FROM users_roles WHERE user_id = :id" ,nativeQuery = true)
    void deleteUsersRolesForUser(Integer id);
}
