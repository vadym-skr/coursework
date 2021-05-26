package com.example.demo.repositories;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository  extends CrudRepository<Book, Integer> {

    List<Book> findAll();

    Page<Book> findAll(Pageable pageable);

}
