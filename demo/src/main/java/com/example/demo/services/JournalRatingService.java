package com.example.demo.services;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.objects.BookRating;
import com.example.demo.entity.objects.Journal;
import com.example.demo.entity.objects.JournalRating;
import com.example.demo.entity.user.User;
import com.example.demo.repositories.BookRatingRepository;
import com.example.demo.repositories.JournalRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JournalRatingService {

    @Autowired
    private JournalRatingRepository journalRatingRepository;

    public JournalRating getJournalRatingByUserAndBook(User user, Journal journal) {
        return journalRatingRepository.findJournalRatingByUserAndBook(user, journal);
    }

    public List<JournalRating> getAll() {
        return journalRatingRepository.findAll();
    }

    public void save(JournalRating journalRating){
        journalRatingRepository.save(journalRating);
    }
    public void deleteJournalRatingForJournal(Integer journalId) {
        journalRatingRepository.deleteJournalRatingForJournal(journalId);
    }
    public void deleteJournalRatingForUser(Integer userId) {
        journalRatingRepository.deleteJournalRatingForUser(userId);
    }


}
