package com.example.demo.repositories;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.user.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository  extends CrudRepository<Book, Integer> {

    List<Book> findAll();

}
