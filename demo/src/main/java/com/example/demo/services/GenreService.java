package com.example.demo.services;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.objects.Genre;
import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.User;
import com.example.demo.repositories.GenreRepository;
import com.example.demo.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public Genre findById(Integer id) {
        return genreRepository.findGenreById(id);
    }
    public Genre findByName(String name) {
        return genreRepository.findGenreByName(name);
    }

    public List<Genre> getAll() {
        Sort sort = Sort.by("name").ascending();
        return genreRepository.findAll(sort);
    }

    public Page<Genre> getForAllGenres(int pageNumber, int pageSize, String sortField, String name) {
        name = "%" + name + "%";

        Sort sort = Sort.by("id").ascending();
        if(sortField != null && !sortField.isEmpty()) {
            if(sortField.equals("name up"))
                sort = Sort.by("name").ascending();
            if(sortField.equals("name down"))
                sort = Sort.by("name").descending();
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Genre> page  = genreRepository.findAll(name, pageable);

        return page;
    }

    public void save(Genre genre) {
        genreRepository.save(genre);
    }

    public void deleteById(Integer id) { genreRepository.deleteById(id); }

    public void deleteGenreForAllBooks(Integer id) {
        genreRepository.deleteGenreForAllBooks(id);
    }
}
