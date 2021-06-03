package com.example.demo.entity.objects;

import com.example.demo.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Getter @Setter
@Entity
@Table(name = "books")
public class Book extends AbstractBook {

    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @NotBlank(message = "cannot be blank!")
    @Pattern(regexp = "^[A-zА-яіІїЇ0-9_@+=*&^%$#!\\[\\]\\-\\s]*$", message = "you can use only this symbols: A-z А-я 0-9 _@+=*&^%$#![]")
    @Pattern(regexp = "^[^\\s].*|", message = "cannot begin or end with a character 'space'")
    @Pattern(regexp = ".*[^\\s]$|", message = "cannot begin or end with a character 'space'")
    @Size(min = 4, max = 40, message = "Size of author is wrong, the name range is from 4 to 40")
    private String author;

    @NotNull(message = "Number is wrong, you can write number from 1 to 999")
    @Min(value = 1, message = "Number is wrong, you can write number from 1 to 999")
    @Max(value = 999, message = "Number is wrong, you can write number from 1 to 999")
    private Integer pages;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "books_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(mappedBy = "book")
    private Set<BookRating> bookRatings = new HashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "favoriteBooks")
    private Set<User> favoriteUsers = new HashSet<>();

    public float getAverageScoreFloat() {
        float averageScore = 0;
        int size = bookRatings.size();
        if(size != 0) {
            for(BookRating bookRating : bookRatings) {
                averageScore += bookRating.getRating();
            }
            averageScore = averageScore/size;
            return averageScore;
        }
        return 0;
    }

    public float getAverageScoreInt() {
        float averageScore = 0;
        int size = bookRatings.size();
        if(size != 0) {
            for(BookRating bookRating : bookRatings) {
                averageScore += bookRating.getRating();
            }
            averageScore = averageScore/size;
            return Math.round(averageScore);
        }
        return 0;
    }

}
