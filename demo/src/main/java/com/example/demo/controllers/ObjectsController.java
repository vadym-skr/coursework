package com.example.demo.controllers;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.objects.Genre;
import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.User;
import com.example.demo.services.BookService;
import com.example.demo.services.GenreService;
import com.example.demo.services.RoleService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/objects")
public class ObjectsController {

    private BookService bookService;
    private GenreService genreService;

    @Autowired
    public ObjectsController(BookService bookService, GenreService genreService) {
        this.bookService = bookService;
        this.genreService = genreService;
    }

    //------------------------------------------------Genre
    @GetMapping("/addGenre")
    public String addGenre(@ModelAttribute("genre") Genre genre) {
        return "objects/addGenre";
    }
    @PostMapping("/addGenre")
    public String createGenre(@ModelAttribute("genre") Genre genre,
                              Model model) {
        if(genre.getName().isBlank()) {
            model.addAttribute("genreErr", "Genre name is empty!");
            model.addAttribute("genre", genre);
            return "objects/addGenre";
        }
        if(genreService.findGenreByName(genre.getName()) != null) {
            model.addAttribute("genreErr", "Genre is already exists!");
            model.addAttribute("genre", genre);
            return "objects/addGenre";
        }
        genreService.save(genre);
        return "redirect:/objects";
    }
    //------------------------------------------------Genre

    //------------------------------------------------Book
    @GetMapping("/addBook")
    public String addBook(@ModelAttribute("book") Book book,
                          @RequestParam(name = "genres", required = false) List<String> genres,
                          Model model) {
        model.addAttribute("allGenres", genreService.getAll());
        model.addAttribute("genres", genres);
        model.addAttribute("book", book);
        return "objects/addBook";
    }
    @PostMapping("/addBook")
    public String createBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                             @RequestParam(name = "genres", required = false) List<String> genres,
                             Model model) {

        if(genres != null) {
            List<Genre> AllGenre = genreService.getAll();
            List<Genre> newGenreForUser = new ArrayList<>();
            for (String genreName : genres) {
                for (Genre genre : AllGenre) {
                    if (genre.getName().equals(genreName)) {
                        newGenreForUser.add(genre);
                    }
                }
            }
            book.setGenres(new HashSet<>(newGenreForUser));
        }
        else {
            book.setGenres(new HashSet<>());
        }

        if (bindingResult.hasFieldErrors("name") ||
                bindingResult.hasFieldErrors("author") ||
                bindingResult.hasFieldErrors("pages") ||
                bindingResult.hasFieldErrors("country") ||
                bindingResult.hasFieldErrors("description")) {
            model.addAttribute("allGenres", genreService.getAll());
            model.addAttribute("genres", genres);
            model.addAttribute("book", book);
            return "objects/addBook";
        }
        return "redirect:/objects";
    }
    //------------------------------------------------Book
}
