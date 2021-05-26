package com.example.demo.services;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.User;
import com.example.demo.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public void create(Book book) {
        bookRepository.save(book);
    }

}
