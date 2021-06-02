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
        if (!(o instanceof Rating rating1)) return false;
        return Objects.equals(getRatingID(), rating1.getRatingID()) && Objects.equals(getUser(), rating1.getUser()) && Objects.equals(getBook(), rating1.getBook()) && Objects.equals(getRating(), rating1.getRating());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRatingID(), getUser(), getBook(), getRating());
    }
}
