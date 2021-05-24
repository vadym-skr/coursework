package com.example.demo.repositories;

import com.example.demo.entity.objects.Genre;
import org.springframework.data.repository.CrudRepository;

public interface GenreRepository  extends CrudRepository<Genre, Integer> {
}
