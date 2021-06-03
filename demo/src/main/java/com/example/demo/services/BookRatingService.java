package com.example.demo.services;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.objects.BookRating;
import com.example.demo.entity.user.User;
import com.example.demo.repositories.BookRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookRatingService {

    @Autowired
    private BookRatingRepository bookRatingRepository;

    public BookRating getBookRatingByUserAndBook(User user, Book book) {
        return bookRatingRepository.findBookRatingByUserAndBook(user, book);
    }

    public List<BookRating> getAll() {
        return bookRatingRepository.findAll();
    }

    public void save(BookRating bookRating){
        bookRatingRepository.save(bookRating);
    }
    public void deleteBookRatingForBook(Integer bookId) {
        bookRatingRepository.deleteBookRatingForBook(bookId);
    }
    public void deleteBookRatingForUser(Integer userId) {
        bookRatingRepository.deleteBookRatingForUser(userId);
    }


}
