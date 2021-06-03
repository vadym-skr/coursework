package com.example.demo.entity.objects;

import com.example.demo.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter @Setter
@Entity
@Table(name = "journals_rating")
public class JournalRating {

    @EmbeddedId
    private JournalRatingID journalRatingID = new JournalRatingID();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("journalId")
    @JoinColumn(name = "journal_id")
    Journal journal;

    private Integer rating;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JournalRating)) return false;
        JournalRating that = (JournalRating) o;
        return Objects.equals(getJournalRatingID(), that.getJournalRatingID()) && Objects.equals(getUser(), that.getUser()) && Objects.equals(getJournal(), that.getJournal()) && Objects.equals(getRating(), that.getRating());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getJournalRatingID(), getUser(), getJournal(), getRating());
    }
}
