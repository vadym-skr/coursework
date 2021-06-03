package com.example.demo.entity.objects;

import com.example.demo.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@Entity
@Table(name = "journal")
public class Journal extends AbstractBook  {

    @Id
    @Column(name = "journal_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @NotBlank(message = "cannot be blank!")
    @Pattern(regexp = "^[A-zА-яіІїЇ0-9_@+=*&^%$#!\\[\\]\\-\\s]*$", message = "you can use only this symbols: A-z А-я 0-9 _@+=*&^%$#![]")
    @Pattern(regexp = "^[^\\s].*|", message = "cannot begin or end with a character 'space'")
    @Pattern(regexp = ".*[^\\s]$|", message = "cannot begin or end with a character 'space'")
    @Size(min = 4, max = 40, message = "Size of author is wrong, the name range is from 4 to 40")
    private String publisher;

    @NotNull(message = "Days is wrong, you can write number days 1 to 9999")
    @Min(value = 1, message = "Days is wrong, you can write days from 1 to 9999")
    @Max(value = 9999, message = "Days is wrong, you can write days from 1 to 9999")
    @Column(name = "publishing_term")
    private Integer publishingTerm;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "journal_genres",
            joinColumns = @JoinColumn(name = "journal_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(mappedBy = "journal")
    private Set<JournalRating> journalRatings = new HashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "favoriteJournal")
    private Set<User> favoriteUsers = new HashSet<>();

    public Journal() {
    }

    public float getAverageScoreFloat() {
        float averageScore = 0;
        int size = journalRatings.size();
        if(size != 0) {
            for(JournalRating journalRating : journalRatings) {
                averageScore += journalRating.getRating();
            }
            averageScore = averageScore/size;
            return averageScore;
        }
        return 0;
    }

    public float getAverageScoreInt() {
        float averageScore = 0;
        int size = journalRatings.size();
        if(size != 0) {
            for(JournalRating journalRating : journalRatings) {
                averageScore += journalRating.getRating();
            }
            averageScore = averageScore/size;
            return Math.round(averageScore);
        }
        return 0;
    }

}
