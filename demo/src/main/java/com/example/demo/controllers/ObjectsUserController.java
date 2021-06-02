package com.example.demo.controllers;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.objects.Rating;
import com.example.demo.entity.user.User;
import com.example.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
public class ObjectsUserController extends AllServicesController {

    @Autowired
    public ObjectsUserController(BookService bookService, GenreService genreService, UserService userService, RatingService ratingService, RoleService roleService, MailSender mailSender) {
        super(bookService, genreService, userService, ratingService, roleService, mailSender);
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
        User currentUser = userService.getCurrentUser();
        if(currentUser != null) {
            model.addAttribute("isFavorite", currentUser.getFavoriteBooks().contains(bookService.findById(bookId)));
        }
        return "showBook";
    }

    @Transactional
    @PostMapping("/books/{id}")
    public String noticeBook(@PathVariable("id") Integer bookId,
                             @RequestParam(name = "rating", required = false) Integer rating,
                             @RequestParam(name = "favorite", required = false) Boolean favorite) {
        // для запобігання помилок , присвоюємо для favorite false коли він дорівнює null
        if(favorite == null) favorite = false;
        // Получаєм теперішнього користувача
        User currentUser = userService.getCurrentUser();
        // Якщо користувач зарієстрований зоходим в метод
        if(currentUser != null) {
            Book currentBook =  bookService.findById(bookId);
            // змінюєм рейтинг якщо він відрізняється від попереднього, або якщо не існує ствоюєм новий
            Rating oldRating = ratingService.getRatingByUserAndBook(currentUser, currentBook);
            // якщо такий рейтинг вже істує то пробуєм змінити, інакше створюєм новий
            if(oldRating != null) {
                // якщо рейтинг не дорівнює попередньому, то змінюєм оцінку, інакше залишаєм
                if(!oldRating.getRating().equals(rating)) {
                    oldRating.setRating(rating);
                    ratingService.save(oldRating);
                }
            }
            else {
                ratingService.save(new Rating(currentUser, currentBook, rating));
            }

            // получаєм старий масив книг
            Set<Book> books = currentUser.getFavoriteBooks();
            // додаєм/видаляєм улюблену книгу для користувача
            // якщо favorite = true пробуэм додати для користувача книгу в улюблиге, інакше пробуєм видалити
            if(favorite) {
                // якщо така книгане не додана в любиме то додаэм, інакше залишаєм як було
                if(!books.contains(currentBook)) {
                    books.add(currentBook);
                    currentUser.setFavoriteBooks(books);
                    userService.save(currentUser);
                }
            }
            else {
                // якщо така книгане додана в любиме то видаляєм, інакше залишаєм як було
                if(books.contains(currentBook)) {
                    books.remove(currentBook);
                    currentUser.setFavoriteBooks(books);
                    userService.save(currentUser);
                }
            }
        }

        return "redirect:/books";
    }

    //----------------------------------------

    @GetMapping("/favoriteBooks")
    public String getAllFavoriteBooks(
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

        Page<Book> books = bookService.getForAllFavoriteBooks(page - 1, size, sort, name, author, country, genreService.findByName(genre), userService.getCurrentUser());

        model.addAttribute("books", books.getContent());
        model.addAttribute("allGenres", genreService.getAll());
        model.addAttribute("maxPage", books.getTotalPages());
        model.addAttribute("page", page);
        model.addAttribute("name", name);
        model.addAttribute("author", author);
        model.addAttribute("country", country);
        model.addAttribute("genre", genre);
        model.addAttribute("sort", sort);
        return "favoriteBooks";
    }

    @Transactional
    @PostMapping("/favoriteBooks")
    public String deleteFavoriteBooks(@RequestParam(name = "bookId") Integer bookId) {
        bookService.deleteFavoriteUserForBook(bookId, userService.getCurrentUser().getId());
        return "redirect:/favoriteBooks";
    }

}
