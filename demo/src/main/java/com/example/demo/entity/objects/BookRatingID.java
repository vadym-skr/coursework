package com.example.demo.entity.objects;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class BookRatingID implements Serializable {

    @Column(name = "user_id")
    protected Integer userId;

    @Column(name = "book_id")
    protected Integer bookId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookRatingID bookRatingID)) return false;
        return Objects.equals(userId, bookRatingID.userId) && Objects.equals(bookId, bookRatingID.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, bookId);
    }
}
