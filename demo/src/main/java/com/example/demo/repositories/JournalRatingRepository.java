package com.example.demo.repositories;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.objects.BookRating;
import com.example.demo.entity.objects.Journal;
import com.example.demo.entity.objects.JournalRating;
import com.example.demo.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JournalRatingRepository extends JpaRepository<JournalRating, Integer> {

    @Query("SELECT r FROM JournalRating as r WHERE r.user = :user AND r.journal = :journal")
    JournalRating findJournalRatingByUserAndBook(User user, Journal journal);

    List<JournalRating> findAll();

    @Modifying
    @Query(value = "DELETE FROM journals_rating WHERE journal_id = :journalId" ,nativeQuery = true)
    void deleteJournalRatingForJournal(Integer journalId);

    @Modifying
    @Query(value = "DELETE FROM journals_rating WHERE user_id = :userId", nativeQuery = true)
    void deleteJournalRatingForUser(Integer userId);

}
