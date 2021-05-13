package com.example.demo.repositories;

import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

// CrudRepository - Інтерфейс для спільних операцій CRUD з репозиторієм певного типу.
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("Select u FROM User u WHERE u.username = :username")
    User findUserByUsername(@Param("username") String username);

    @Query("Select u FROM User u WHERE u.id = :id")
    User findUserById(@Param("id") Integer id);

    List<User> findAll();
    Page<User> findByUsernameLikeAndEmailLikeAndEnabledOrEnabledLikeAndRolesNameLike(String username, String email, boolean enabledOne, boolean enabledTwo, String roleName, Pageable pageable);
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
    //SELECT user_id, username, password, enabled, email FROM
    //                    ((SELECT@n:=@n + 1 as rownum, u.* FROM users u) as t, (SELECT @n:=0)n)
    //                    where rownum >=:from LIMIT :amount;

    //@Query(value = "SELECT user_id, username, password, enabled, email FROM" +
    //        " ((SELECT@n \\:= @n+1 as rownum, u.* FROM users u) as t, (SELECT@n \\:= 0) as n)" +
    //        " where rownum >=:from LIMIT :amount",
    //        nativeQuery = true)

//    Page<User> findAll(Pageable pageRequest);
//    @Query(value = "SELECT user_id, username, password, enabled, email FROM" +
//                   " ((SELECT@n \\:= @n+1 as rownum, u.* FROM users u) as t, (SELECT@n \\:= 0) as n)" +
//                   " where rownum >=:from LIMIT :amount",
//            nativeQuery = true)
//    List<User> findUsersFoo(@Param("from") int from,
//                            @Param("amount") int amount);

    boolean existsUserByUsername(String username);

    long count();
}
