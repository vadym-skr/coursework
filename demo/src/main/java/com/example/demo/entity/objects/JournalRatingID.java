package com.example.demo.entity.objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class JournalRatingID implements Serializable {

    @Column(name = "user_id")
    protected Integer userId;

    @Column(name = "journal_id")
    protected Integer journalId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JournalRatingID)) return false;
        JournalRatingID that = (JournalRatingID) o;
        return Objects.equals(userId, that.userId) && Objects.equals(journalId, that.journalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, journalId);
    }
}
