package com.example.demo.repositories;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.objects.Genre;
import com.example.demo.entity.objects.Rating;
import com.example.demo.entity.objects.RatingID;
import com.example.demo.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RatingRepository extends CrudRepository<Rating, Integer> {

    @Query("SELECT r FROM Rating as r WHERE r.user = :user AND r.book = :book")
    Rating findRatingByUserAndBook(User user, Book book);

    List<Rating> findAll();

    @Modifying
    @Query(value = "DELETE FROM books_rating WHERE book_id = :id" ,nativeQuery = true)
    void deleteBookRatingForBook(Integer id);

    @Modifying
    @Query(value = "DELETE FROM books_rating WHERE user_id = :userId" ,nativeQuery = true)
    void deleteBookRatingForUser(Integer userId);

}
