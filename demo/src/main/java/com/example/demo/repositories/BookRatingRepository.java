package com.example.demo.repositories;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.objects.BookRating;
import com.example.demo.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRatingRepository extends JpaRepository<BookRating, Integer> {

    @Query("SELECT r FROM BookRating as r WHERE r.user = :user AND r.book = :book")
    BookRating findBookRatingByUserAndBook(User user, Book book);

    List<BookRating> findAll();

    @Modifying
    @Query(value = "DELETE FROM books_rating WHERE book_id = :bookId" ,nativeQuery = true)
    void deleteBookRatingForBook(Integer bookId);

    @Modifying
    @Query(value = "DELETE FROM books_rating WHERE user_id = :userId" ,nativeQuery = true)
    void deleteBookRatingForUser(Integer userId);

}
