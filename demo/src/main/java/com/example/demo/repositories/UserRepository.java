package com.example.demo.repositories;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.List;

// CrudRepository - Інтерфейс для спільних операцій CRUD з репозиторієм певного типу.
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("Select u FROM User u WHERE u.username = :username")
    User findUserByUsername(@Param("username") String username);

    @Query("Select u FROM User u WHERE u.id = :id")
    User findUserById(@Param("id") Integer id);

    List<User> findAll();
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

    @Query(value = "SELECT u FROM User as u" +
            " where u.username like :username" +
            " AND u.email like :email" +
            " AND (u.enabled =:enabledOne or u.enabled =:enabledTwo)" +
            " AND :role member u.roles")
    Page<User> findForPageAllUsers(String username, String email, boolean enabledOne, boolean enabledTwo, Role role, Pageable pageable);

    void deleteUserById(Integer id);

    @Modifying
    @Query(value = "DELETE FROM favorite_books_for_users WHERE user_id = :userId", nativeQuery = true)
    void deleteAllFavoriteBookForUser(Integer userId);
}
