package com.example.demo.entity.objects;

import com.example.demo.entity.user.User;
import org.hibernate.Hibernate;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class RatingID implements Serializable {

    protected User user;
    protected Book book;

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }


    public RatingID() {
    }
    public RatingID(User user, Book book) {
        this.user = user;
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RatingID ratingID = (RatingID) o;

        if (!Objects.equals(user, ratingID.user)) return false;
        if (!Objects.equals(book, ratingID.book)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (book != null ? book.hashCode() : 0);
        return result;
    }
}
