package com.example.demo.entity.objects;

import com.example.demo.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

//https://www.codejava.net/frameworks/hibernate/hibernate-many-to-many-association-with-extra-columns-in-join-table-example
@Getter @Setter
@Entity
@Table(name = "books_rating")
//@AssociationOverrides({
//        @AssociationOverride(name = "ratingID.user",
//                joinColumns = @JoinColumn(name = "user_id")),
//        @AssociationOverride(name = "ratingID.book",
//                joinColumns = @JoinColumn(name = "book_id")) })
public class Rating {

    @EmbeddedId
    private RatingID ratingID = new RatingID();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    Book book;

    private Integer rating;

////    @EmbeddedId
//    public RatingID getRatingID() {
//        return ratingID;
//    }
//
//    public void setRatingID(RatingID primaryKey) {
//        this.ratingID = primaryKey;
//    }
//
////    @Transient
//    public User getUser() {
//        return getRatingID().getUser();
//    }
//
//    public void setUser(User user) {
//        getRatingID().setUser(user);
//    }
//
////    @Transient
//    public Book getBook() {
//        return getRatingID().getBook();
//    }
//
//    public void setBook(Book book) {
//        getRatingID().setBook(book);
//    }
//
//    public Integer getRating() {
//        return rating;
//    }
//
//    public void setRating(Integer rating) {
//        this.rating = rating;
//    }

    public Rating() {
    }

    public Rating(User user, Book book, Integer rating) {
        this.user = user;
        this.book = book;
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
