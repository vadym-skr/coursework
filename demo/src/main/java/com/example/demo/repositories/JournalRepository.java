package com.example.demo.repositories;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.objects.Genre;
import com.example.demo.entity.objects.Journal;
import com.example.demo.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JournalRepository extends JpaRepository<Journal, Integer> {

    Journal findJournalById(Integer id);
    Journal findJournalByName(String name);

    List<Journal> findAll();

    @Query(value = "SELECT j FROM Journal as j" +
            " where j.name like :name" +
            " AND j.country like :country" +
            " AND j.publisher like :publisher")

    Page<Journal> findAll(String name, String publisher, String country, Pageable pageable);

    @Query(value = "SELECT j FROM Journal as j" +
            " where j.name like :name" +
            " AND j.country like :country" +
            " AND j.publisher like :publisher" +
            " AND :genre member j.genres")
    Page<Journal> findAll(String name, String country, String publisher, Genre genre, Pageable pageable);

    @Query(value = "SELECT j FROM Journal as j" +
            " where j.name like :name" +
            " AND j.country like :country" +
            " AND j.publisher like :publisher" +
            " AND :user member j.favoriteUsers")
    Page<Journal> findAll(String name, String country, String publisher, User user, Pageable pageable);

    @Query(value = "SELECT j FROM Journal as j" +
            " where j.name like :name" +
            " AND j.country like :country" +
            " AND j.publisher like :publisher" +
            " AND :genre member j.genres" +
            " AND :user member j.favoriteUsers")
    Page<Journal> findAll(String name, String country, String publisher, Genre genre, User user, Pageable pageable);

    @Modifying
    @Query(value = "DELETE FROM favorite_journals_for_users WHERE journal_id = :journalId AND user_id = :userId", nativeQuery = true)
    void deleteFavoriteBookForUser(Integer journalId, Integer userId);

    @Modifying
    @Query(value = "DELETE FROM favorite_journals_for_users WHERE journal_id = :journalId", nativeQuery = true)
    void deleteAllFavoriteBookForUser(Integer journalId);

}