package com.example.demo.entity.objects;

import com.example.demo.entity.user.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter @Setter
@Entity
@Table(name = "books")
public class Book implements Serializable {
    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Size(min = 4, max = 40, message = "Size of name is wrong, the name range is from 4 to 40")
    private String name;
    @Size(min = 4, max = 40, message = "Size of author is wrong, the name range is from 4 to 40")
    private String author;
    @NotNull(message = "Number is wrong, you can write number from 1 to 999")
    @Min(value = 1, message = "Number is wrong, you can write number from 1 to 999")
    @Max(value = 999, message = "Number is wrong, you can write number from 1 to 999")
    private Integer pages;
    @Size(min = 4, max = 40, message = "Size of country is wrong, the name range is from 4 to 40")
    private String country;

    private String description;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "books_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "ratingID.book")
    private List<Rating> ratings = new ArrayList<>();

    public float getAverageScoreFloat() {
        float averageScore = 0;
        int size = ratings.size();
        if(size != 0) {
            for(Rating rating : ratings) {
                averageScore += rating.getRating();
            }
            averageScore = averageScore/size;
            return averageScore;
        }
        return 0;
    }

    public float getAverageScoreInt() {
        float averageScore = 0;
        int size = ratings.size();
        if(size != 0) {
            for(Rating rating : ratings) {
                averageScore += rating.getRating();
            }
            averageScore = averageScore/size;
            return Math.round(averageScore);
        }
        return 0;
    }

}
