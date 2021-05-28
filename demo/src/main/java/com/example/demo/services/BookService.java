package com.example.demo.services;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.objects.Genre;
import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.User;
import com.example.demo.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book findById(Integer id) {
        return bookRepository.findBookById(id);
    }
    public Book findByName(String name) {
        return bookRepository.findBookByName(name);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Page<Book> getForAllBooks(int pageNumber, int pageSize, String sortField, String name, String author, String country, Genre genre) {
        name = "%" + name + "%";
        author = "%" + author + "%";
        country = "%" + country + "%";

        Sort sort = Sort.by("id").ascending();
        if(sortField != null && !sortField.isEmpty()) {
            if(sortField.equals("name up"))
                sort = Sort.by("name").ascending();
            if(sortField.equals("name down"))
                sort = Sort.by("name").descending();

            if(sortField.equals("author up"))
                sort = Sort.by("author").ascending();
            if(sortField.equals("author down"))
                sort = Sort.by("author").descending();

            if(sortField.equals("country up"))
                sort = Sort.by("country").ascending();
            if(sortField.equals("country down"))
                sort = Sort.by("country").descending();

            if(sortField.equals("pages up"))
                sort = Sort.by("pages").ascending();
            if(sortField.equals("pages down"))
                sort = Sort.by("pages").descending();
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Book> page = null;

        if(genre == null)
            page = bookRepository.findAll(name, author, country, pageable);
        else
            page = bookRepository.findAll(name, author, country, genre, pageable);

        return page;
    }

    public void save(Book book) {
        bookRepository.save(book);
    }
    public void deleteById(Integer id) {bookRepository.deleteById(id);}
}
