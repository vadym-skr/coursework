package com.example.demo.entity.objects;

import com.example.demo.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter @Setter
@Entity
@Table(name = "books_rating")
public class BookRating {

    @EmbeddedId
    private BookRatingID bookRatingID = new BookRatingID();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    Book book;

    private Integer rating;

    public BookRating() {
    }

    public BookRating(User user, Book book, Integer rating) {
        this.user = user;
        this.book = book;
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookRating bookRating1)) return false;
        return Objects.equals(getBookRatingID(), bookRating1.getBookRatingID()) && Objects.equals(getUser(), bookRating1.getUser()) && Objects.equals(getBook(), bookRating1.getBook()) && Objects.equals(getRating(), bookRating1.getRating());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBookRatingID(), getUser(), getBook(), getRating());
    }
}
