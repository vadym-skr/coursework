package com.example.demo.repositories;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.objects.Genre;
import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository  extends JpaRepository<Book, Integer> {

    Book findBookById(Integer id);
    Book findBookByName(String name);

    List<Book> findAll();

    @Query(value = "SELECT b FROM Book as b" +
            " where b.name like :name" +
            " AND b.author like :author" +
            " AND b.country like :country")
    Page<Book> findAll(String name, String author, String country, Pageable pageable);

    @Query(value = "SELECT b FROM Book as b" +
            " where b.name like :name" +
            " AND b.author like :author" +
            " AND b.country like :country" +
            " AND :genre member b.genres")
    Page<Book> findAll(String name, String author, String country, Genre genre, Pageable pageable);

    @Query(value = "SELECT b FROM Book as b" +
            " where b.name like :name" +
            " AND b.author like :author" +
            " AND b.country like :country" +
            " AND :user member b.favoriteUsers")
    Page<Book> findAll(String name, String author, String country, User user, Pageable pageable);

    @Query(value = "SELECT b FROM Book as b" +
            " where b.name like :name" +
            " AND b.author like :author" +
            " AND b.country like :country" +
            " AND :genre member b.genres" +
            " AND :user member b.favoriteUsers")
    Page<Book> findAll(String name, String author, String country, Genre genre, User user, Pageable pageable);

    @Modifying
    @Query(value = "DELETE FROM favorite_books_for_users WHERE book_id = :bookId AND user_id = :userId", nativeQuery = true)
    void deleteFavoriteBookForUser(Integer bookId, Integer userId);

    @Modifying
    @Query(value = "DELETE FROM favorite_books_for_users WHERE book_id = :bookId", nativeQuery = true)
    void deleteAllFavoriteBookForUser(Integer bookId);
}
