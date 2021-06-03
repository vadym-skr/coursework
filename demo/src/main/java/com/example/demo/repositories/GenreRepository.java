package com.example.demo.repositories;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.objects.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GenreRepository  extends JpaRepository<Genre, Integer> {

    Genre findGenreById(Integer id);
    Genre findGenreByName(String name);

    List<Genre> findAll(Sort sort);

    @Query(value = "SELECT g FROM Genre as g" +
            " where g.name like :name")
    Page<Genre> findAll(String name, Pageable pageable);

    @Modifying
    @Query(value = "DELETE FROM books_genres as g" +
            " where g.genre_id = :id", nativeQuery = true)
    void deleteGenreForAllBooks(Integer id);

    @Modifying
    @Query(value = "DELETE FROM journal_genres as g" +
            " where g.genre_id = :id", nativeQuery = true)
    void deleteGenreForAllJournals(Integer id);
}
