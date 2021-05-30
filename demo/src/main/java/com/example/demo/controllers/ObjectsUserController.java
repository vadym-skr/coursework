package com.example.demo.controllers;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.objects.Rating;
import com.example.demo.entity.user.User;
import com.example.demo.services.BookService;
import com.example.demo.services.GenreService;
import com.example.demo.services.RatingService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ObjectsUserController {

    private BookService bookService;
    private GenreService genreService;
    private UserService userService;
    private RatingService ratingService;

    @Autowired
    public ObjectsUserController(BookService bookService, GenreService genreService, UserService userService, RatingService ratingService) {
        this.bookService = bookService;
        this.genreService = genreService;
        this.userService = userService;
        this.ratingService = ratingService;
    }

    @GetMapping("/books")
    public String getAllBooks(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "country", required = false) String country,
            @RequestParam(name = "genre", required = false) String genre,
            @RequestParam(name = "sort", required = false) String sort,
            Model model) {
        int size = 4;

        if(page == null) { page = 1; }
        if(sort == null) { sort = ""; }
        if(name == null || name.isBlank()) { name = ""; }
        if(author == null || author.isBlank()) { author = ""; }
        if(country == null || country.isBlank()) { country = ""; }
        if(genre == null || genre.isBlank()) { genre = ""; }

        Page<Book> books = bookService.getForAllBooks(page - 1, size, sort, name, author, country, genreService.findByName(genre));

        model.addAttribute("books", books.getContent());
        model.addAttribute("allGenres", genreService.getAll());
        model.addAttribute("maxPage", books.getTotalPages());
        model.addAttribute("page", page);
        model.addAttribute("name", name);
        model.addAttribute("author", author);
        model.addAttribute("country", country);
        model.addAttribute("genre", genre);
        model.addAttribute("sort", sort);
        return "books";
    }

    @GetMapping("/books/{id}")
    public String showBook(@PathVariable("id") Integer bookId, Model model) {
        model.addAttribute("book", bookService.findById(bookId));
        return "showBook";
    }

    @PostMapping("/books/{id}")
    public String ratingBook(@PathVariable("id") Integer bookId,
                             @RequestParam(name = "rating", required = false) Integer rating) {
        // Якщо оцінка не дорівнює null, то обновлюєм/добавляєм нову оцінку
        if(rating != null) {
            // Получаєм теперішнього користувача
            User currentUser = userService.getCurrentUser();
            // Якщо користувач не зарієстрований, то оцінка не буде зроблена
            if (currentUser != null) {
                // пробуєм получити стару оцінку
                Rating oldRating = ratingService.getRatingByUserAndBook(currentUser, bookService.findById(bookId));
                // якщо оцінка вже існує, то обновлюєм, якщо вона відрізняється від попередньої
                if (oldRating != null) {
                    // якщо оцінка відрізняється змінюєм, інакше залишаєм все як було
                    if(!oldRating.getRating().equals(rating)) {
                        // просто змінюєм стару оцінку на нову
                        oldRating.setRating(rating);
                        // обновлюєм
                        ratingService.save(oldRating);
                    }
                }
                // інакше створюєм нову оцінку
                else {
                    // створюємо новий рейтенг для книги
                    Rating newRating = new Rating(userService.getCurrentUser(), bookService.findById(bookId), rating);
                    // зберігаєм
                    ratingService.save(newRating);
                }
            }
        }
        return "redirect:/books";
    }

}
