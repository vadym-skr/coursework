package com.example.demo.entity.objects;

import com.example.demo.entity.user.User;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class RatingID implements Serializable {

//    @ManyToOne(cascade = CascadeType.ALL)
    @Column(name = "user_id")
    protected Integer userId;
//    @ManyToOne(cascade = CascadeType.ALL)
    @Column(name = "book_id")
    protected Integer bookId;

////    @ManyToOne
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
////    @ManyToOne
//    public Book getBook() {
//        return book;
//    }
//
//    public void setBook(Book book) {
//        this.book = book;
//    }


    public RatingID() {
    }
    public RatingID(Integer userId, Integer bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RatingID)) return false;
        RatingID ratingID = (RatingID) o;
        return Objects.equals(userId, ratingID.userId) && Objects.equals(bookId, ratingID.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, bookId);
    }
}
