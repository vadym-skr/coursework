package com.example.demo.services;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.objects.Genre;
import com.example.demo.entity.objects.Journal;
import com.example.demo.entity.user.User;
import com.example.demo.repositories.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;

    public Journal findById(Integer id) {
        return journalRepository.findJournalById(id);
    }
    public Journal findByName(String name) {
        return journalRepository.findJournalByName(name);
    }

    public List<Journal> findAll() {
        return journalRepository.findAll();
    }

    public Page<Journal> getForAllJournals(int pageNumber, int pageSize, String sortField, String name, String publisher, String country, Genre genre) {
        name = "%" + name + "%";
        publisher = "%" + publisher + "%";
        country = "%" + country + "%";

        Sort sort = Sort.by("id").ascending();
        if(sortField != null && !sortField.isEmpty()) {
            if(sortField.equals("name up"))
                sort = Sort.by("name").ascending();
            if(sortField.equals("name down"))
                sort = Sort.by("name").descending();

            if(sortField.equals("publisher up"))
                sort = Sort.by("publisher").ascending();
            if(sortField.equals("publisher down"))
                sort = Sort.by("publisher").descending();

            if(sortField.equals("country up"))
                sort = Sort.by("country").ascending();
            if(sortField.equals("country down"))
                sort = Sort.by("country").descending();

            if(sortField.equals("publishing term up"))
                sort = Sort.by("publishingTerm").ascending();
            if(sortField.equals("publishing term down"))
                sort = Sort.by("publishingTerm").descending();
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Journal> page = null;

        if(genre == null)
            page = journalRepository.findAll(name, publisher, country, pageable);
        else
            page = journalRepository.findAll(name, publisher, country, genre, pageable);

        return page;
    }

    public Page<Journal> getForAllFavoriteJournals(int pageNumber, int pageSize, String sortField, String name, String publisher, String country, Genre genre, User user) {
        name = "%" + name + "%";
        publisher = "%" + publisher + "%";
        country = "%" + country + "%";

        Sort sort = Sort.by("id").ascending();
        if(sortField != null && !sortField.isEmpty()) {
            if(sortField.equals("name up"))
                sort = Sort.by("name").ascending();
            if(sortField.equals("name down"))
                sort = Sort.by("name").descending();

            if(sortField.equals("publisher up"))
                sort = Sort.by("publisher").ascending();
            if(sortField.equals("publisher down"))
                sort = Sort.by("publisher").descending();

            if(sortField.equals("country up"))
                sort = Sort.by("country").ascending();
            if(sortField.equals("country down"))
                sort = Sort.by("country").descending();

            if(sortField.equals("publishing term up"))
                sort = Sort.by("publishingTerm").ascending();
            if(sortField.equals("publishing term down"))
                sort = Sort.by("publishingTerm").descending();
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Journal> page = null;

        if(genre == null)
            page = journalRepository.findAll(name, publisher, country, user, pageable);
        else
            page = journalRepository.findAll(name, publisher, country, genre, user, pageable);

        return page;
    }

    public void save(Journal journal) {
        journalRepository.save(journal);
    }
    public void deleteById(Integer id) {
        journalRepository.deleteById(id);
    }
    public void deleteFavoriteUserForJournal(Integer journalId, Integer userId) {
        journalRepository.deleteFavoriteJournalForUser(journalId, userId);
    }
    public void deleteAllFavoriteUserForJournal(Integer journalId) {
        journalRepository.deleteAllFavoriteJournalForUser(journalId);
    }

}
