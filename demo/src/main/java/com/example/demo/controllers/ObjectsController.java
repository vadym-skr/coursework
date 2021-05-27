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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
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
        book.setGenres(toSetOfGenre(genres));

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

        if (bookService.findByName(book.getName()) != null) {
            model.addAttribute("allGenres", genreService.getAll());
            model.addAttribute("genres", genres);
            model.addAttribute("book", book);
            model.addAttribute("nameErr", "Name of book is already exists!");
            return "objects/addBook";
        }

        bookService.save(book);

        return "redirect:/objects";
    }

    @GetMapping("/books")
    public String getAllUsers(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "country", required = false) String country,
            @RequestParam(name = "genre", required = false) String genre,
            @RequestParam(name = "sort", required = false) String sort,
            Model model) {
        int size = 3;
        if(genre != null) {
            System.out.println("sdasd");
        }
        if(page == null) { page = 1; }
        if(sort == null) { sort = ""; }
        if(name == null || name.isBlank()) { name = ""; }
        if(author == null || author.isBlank()) { author = ""; }
        if(country == null || country.isBlank()) { country = ""; }
        if(genre == null || genre.isBlank()) { genre = ""; }

        Page<Book> books = bookService.getForAllBooks(page - 1, size, sort, name, author, country, genreService.findGenreByName(genre));

        model.addAttribute("books", books.getContent());
        model.addAttribute("allGenres", genreService.getAll());
        model.addAttribute("maxPage", books.getTotalPages());
        model.addAttribute("page", page);
        model.addAttribute("name", name);
        model.addAttribute("author", author);
        model.addAttribute("country", country);
        model.addAttribute("genre", genre);
        model.addAttribute("sort", sort);
        return "objects/books";
    }

    @GetMapping("/editBook/{id}")
    public String editBook(@PathVariable Integer id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        model.addAttribute("allGenres", genreService.getAll());
        return "objects/editBook";
    }

    @PostMapping("/editBook/{id}")
    public String updateBook(@ModelAttribute(name = "book") @Valid Book book, BindingResult bindingResult,
                             @RequestParam(name = "genres", required = false) List<String> genres,
                             Model model) {
        Book oldBook = bookService.findById(book.getId());
        book.setGenres(toSetOfGenre(genres));

        if (bindingResult.hasFieldErrors("name") ||
                bindingResult.hasFieldErrors("author") ||
                bindingResult.hasFieldErrors("pages") ||
                bindingResult.hasFieldErrors("country") ||
                bindingResult.hasFieldErrors("description")) {
            model.addAttribute("allGenres", genreService.getAll());
            model.addAttribute("genres", genres);
            model.addAttribute("book", book);
            return "objects/editBook";
        }

        if (bookService.findByName(book.getName()) != null && !book.getName().equals(oldBook.getName())) {
            model.addAttribute("allGenres", genreService.getAll());
            model.addAttribute("genres", genres);
            model.addAttribute("book", book);
            model.addAttribute("nameErr", "Name of book is already exists!");
            return "objects/editBook";
        }

        bookService.save(book);

        return "redirect:/objects/books";
    }

    private Set<Genre> toSetOfGenre(List<String> genres) {
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
            return new HashSet<>(newGenreForUser);
        }
        return new HashSet<>();
    }

    //------------------------------------------------Book
}
