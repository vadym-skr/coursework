package com.example.demo.services;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.objects.Rating;
import com.example.demo.entity.objects.RatingID;
import com.example.demo.entity.user.User;
import com.example.demo.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public Rating getRatingByUserAndBook(User user, Book book) {
        return ratingRepository.findRatingByUserAndBook(user, book);
//        return null;
    }

    public List<Rating> getAll() {
        return ratingRepository.findAll();
    }

    public void save(Rating rating){
        ratingRepository.save(rating);
    }
    public void deleteBookRatingForBook(Integer bookId) {
        ratingRepository.deleteBookRatingForBook(bookId);
    }
    public void deleteBookRatingForUser(Integer userId) {
        ratingRepository.deleteBookRatingForUser(userId);
    }


}
