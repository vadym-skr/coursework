package com.example.demo.services;

import com.example.demo.entity.objects.Genre;
import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.User;
import com.example.demo.repositories.GenreRepository;
import com.example.demo.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    public Genre findGenreByName(String name) {
        return genreRepository.findGenreByName(name);
    }

    public void save(Genre genre) {
        genreRepository.save(genre);
    }

}
