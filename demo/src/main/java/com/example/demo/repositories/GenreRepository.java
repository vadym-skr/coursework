package com.example.demo.repositories;

import com.example.demo.entity.objects.Genre;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GenreRepository  extends CrudRepository<Genre, Integer> {

    List<Genre> findAll();

    Genre findGenreByName(String name);
}
