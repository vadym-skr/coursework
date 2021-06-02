package com.example.demo.entity.objects;

import com.example.demo.entity.user.User;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class RatingID implements Serializable {

    @Column(name = "user_id")
    protected Integer userId;

    @Column(name = "book_id")
    protected Integer bookId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RatingID ratingID)) return false;
        return Objects.equals(userId, ratingID.userId) && Objects.equals(bookId, ratingID.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, bookId);
    }
}
