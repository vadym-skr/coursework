package com.example.demo.entity.objects;

import com.example.demo.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

//https://www.codejava.net/frameworks/hibernate/hibernate-many-to-many-association-with-extra-columns-in-join-table-example
@Entity
@Table(name = "books_rating")
@AssociationOverrides({
        @AssociationOverride(name = "ratingID.user",
                joinColumns = @JoinColumn(name = "user_id")),
        @AssociationOverride(name = "ratingID.book",
                joinColumns = @JoinColumn(name = "book_id")) })
public class Rating {

    private RatingID ratingID = new RatingID();
    private Integer rating;

    @EmbeddedId
    public RatingID getRatingID() {
        return ratingID;
    }

    public void setRatingID(RatingID primaryKey) {
        this.ratingID = primaryKey;
    }

    @Transient
    public User getUser() {
        return getRatingID().getUser();
    }

    public void setUser(User user) {
        getRatingID().setUser(user);
    }

    @Transient
    public Book getBook() {
        return getRatingID().getBook();
    }

    public void setBook(Book book) {
        getRatingID().setBook(book);
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Rating() {
    }

    public Rating(User user, Book book, Integer rating) {
        this.ratingID = new RatingID(user, book);
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rating)) return false;
        Rating rating1 = (Rating) o;
        return Objects.equals(getRatingID(), rating1.getRatingID()) && Objects.equals(getRating(), rating1.getRating());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRatingID(), getRating());
    }
}
