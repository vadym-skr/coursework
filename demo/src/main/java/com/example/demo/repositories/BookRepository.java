package com.example.demo.repositories;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.objects.Genre;
import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository  extends CrudRepository<Book, Integer> {

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

}
