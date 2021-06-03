package com.example.demo.controllers;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.objects.Genre;
import com.example.demo.entity.objects.Journal;
import com.example.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/editor")
public class ObjectsController extends AllServicesController {

    @Autowired
    public ObjectsController(BookService bookService, JournalService journalService ,GenreService genreService, UserService userService, BookRatingService bookRatingService, JournalRatingService journalRatingService, RoleService roleService, MailSender mailSender) {
        super(bookService, journalService, genreService, userService, bookRatingService, journalRatingService, roleService, mailSender);
    }

    @GetMapping()
    public String object(){
        return "editor/editor";
    }

    //------------------------------------------------Genre

    @GetMapping("/genres")
    public String getAllGenres(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "sort", required = false) String sort,
            Model model) {
        int size = 3;

        if(page == null) { page = 1; }
        if(sort == null) { sort = ""; }
        if(name == null || name.isBlank()) { name = ""; }

        Page<Genre> genres = genreService.getForAllGenres(page - 1, size, sort, name);

        model.addAttribute("genres", genres.getContent());
        model.addAttribute("maxPage", genres.getTotalPages());
        model.addAttribute("page", page);
        model.addAttribute("name", name);
        model.addAttribute("sort", sort);
        return "editor/genres";
    }

    @Transactional
    @PostMapping("/genres")
    public String deleteGenre(@RequestParam(name = "genreId") Integer id) {
        genreService.deleteGenreForAllBooksAndJournals(id);
        genreService.deleteById(id);
        return "redirect:/editor/genres";
    }

    @GetMapping("/addGenre")
    public String addGenre(@ModelAttribute("genre") Genre genre) {
        return "editor/addGenre";
    }
    @PostMapping("/addGenre")
    public String createGenre(@ModelAttribute("genre") Genre genre,
                              Model model) {
        if(genre.getName().isBlank()) {
            model.addAttribute("genreErr", "Genre name is empty!");
            model.addAttribute("genre", genre);
            return "editor/addGenre";
        }
        if(genreService.findByName(genre.getName()) != null) {
            model.addAttribute("genreErr", "Genre is already exists!");
            model.addAttribute("genre", genre);
            return "editor/addGenre";
        }
        genreService.save(genre);
        return "redirect:/editor";
    }

    @GetMapping("/editGenre/{id}")
    public String editGenre(@PathVariable Integer id, Model model) {
        model.addAttribute("genre", genreService.findById(id));
        return "editor/editGenre";
    }

    @PostMapping("/editGenre/{id}")
    public String updateGenre(@ModelAttribute(name = "genre") @Valid Genre genre,
                             Model model) {
        Genre oldGenre = genreService.findById(genre.getId());

        if (genreService.findByName(genre.getName()) != null && !genre.getName().equals(oldGenre.getName())) {
            model.addAttribute("genre", genre);
            model.addAttribute("nameErr", "Name of genre is already exists!");
            return "editor/editGenre";
        }

        genreService.save(genre);

        return "redirect:/editor/genres";
    }

    //------------------------------------------------Genre

    //------------------------------------------------Book

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
        return "editor/books";
    }

    @Transactional
    @PostMapping("/books")
    public String deleteBook(@RequestParam(name = "bookId") Integer id) {
        bookService.deleteAllFavoriteUserForBook(id);
        bookRatingService.deleteBookRatingForBook(id);
        bookService.deleteById(id);
        return "redirect:/editor/books";
    }

    @GetMapping("/addBook")
    public String addBook(@ModelAttribute("book") Book book,
                          @RequestParam(name = "genres", required = false) List<String> genres,
                          Model model) {
        model.addAttribute("allGenres", genreService.getAll());
        model.addAttribute("genres", genres);
        model.addAttribute("book", book);
        return "editor/addBook";
    }

    @PostMapping("/addBook")
    public String createBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                             @RequestParam(name = "genres", required = false) List<String> genres,
                             Model model) {
        book.setGenres(toSetOfGenre(genres));

        if(book.getPages() == null) {
            model.addAttribute("allGenres", genreService.getAll());
            model.addAttribute("genres", genres);
            model.addAttribute("book", book);
            model.addAttribute("pagesErr", "Pages has wrong format!");
            return "editor/addBook";
        }

        if (bindingResult.hasFieldErrors("name") ||
                bindingResult.hasFieldErrors("author") ||
                bindingResult.hasFieldErrors("pages") ||
                bindingResult.hasFieldErrors("country") ||
                bindingResult.hasFieldErrors("description")) {
            model.addAttribute("allGenres", genreService.getAll());
            model.addAttribute("genres", genres);
            model.addAttribute("book", book);
            return "editor/addBook";
        }

        if (bookService.findByName(book.getName()) != null) {
            model.addAttribute("allGenres", genreService.getAll());
            model.addAttribute("genres", genres);
            model.addAttribute("book", book);
            model.addAttribute("nameErr", "Name of book is already exists!");
            return "editor/addBook";
        }

        bookService.save(book);

        return "redirect:/editor";
    }

    @GetMapping("/editBook/{id}")
    public String editBook(@PathVariable Integer id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        model.addAttribute("allGenres", genreService.getAll());
        return "editor/editBook";
    }

    @PostMapping("/editBook/{id}")
    public String updateBook(@ModelAttribute(name = "book") @Valid Book book, BindingResult bindingResult,
                             @RequestParam(name = "genres", required = false) List<String> genres,
                             Model model) {
        Book oldBook = bookService.findById(book.getId());
        book.setGenres(toSetOfGenre(genres));

        if(book.getPages() == null) {
            model.addAttribute("allGenres", genreService.getAll());
            model.addAttribute("genres", genres);
            model.addAttribute("book", book);
            model.addAttribute("pagesErr", "Pages has wrong format!");
        }

        if (bindingResult.hasFieldErrors("name") ||
                bindingResult.hasFieldErrors("author") ||
                bindingResult.hasFieldErrors("pages") ||
                bindingResult.hasFieldErrors("country") ||
                bindingResult.hasFieldErrors("description")) {
            model.addAttribute("allGenres", genreService.getAll());
            model.addAttribute("genres", genres);
            model.addAttribute("book", book);
            return "editor/editBook";
        }

        if (bookService.findByName(book.getName()) != null && !book.getName().equals(oldBook.getName())) {
            model.addAttribute("allGenres", genreService.getAll());
            model.addAttribute("genres", genres);
            model.addAttribute("book", book);
            model.addAttribute("nameErr", "Name of book is already exists!");
            return "editor/editBook";
        }

        book.setBookRatings(oldBook.getBookRatings());
        book.setFavoriteUsers(oldBook.getFavoriteUsers());
        bookService.save(book);

        return "redirect:/editor/books";
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

    //------------------------------------------------Journal

    @GetMapping("/journals")
    public String getAllJournal(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "publisher", required = false) String publisher,
            @RequestParam(name = "country", required = false) String country,
            @RequestParam(name = "genre", required = false) String genre,
            @RequestParam(name = "sort", required = false) String sort,
            Model model) {
        int size = 4;

        if(page == null) { page = 1; }
        if(sort == null) { sort = ""; }
        if(name == null || name.isBlank()) { name = ""; }
        if(publisher == null || publisher.isBlank()) { publisher = ""; }
        if(country == null || country.isBlank()) { country = ""; }
        if(genre == null || genre.isBlank()) { genre = ""; }

        Page<Journal> journals = journalService.getForAllJournals(page - 1, size, sort, name, publisher, country, genreService.findByName(genre));

        model.addAttribute("journals", journals.getContent());
        model.addAttribute("allGenres", genreService.getAll());
        model.addAttribute("maxPage", journals.getTotalPages());
        model.addAttribute("page", page);
        model.addAttribute("name", name);
        model.addAttribute("publisher", publisher);
        model.addAttribute("country", country);
        model.addAttribute("genre", genre);
        model.addAttribute("sort", sort);
        return "editor/journals";
    }

    @Transactional
    @PostMapping("/journals")
    public String deleteJournal(@RequestParam(name = "journalId") Integer id) {
        journalService.deleteAllFavoriteUserForJournal(id);
        journalRatingService.deleteJournalRatingForJournal(id);
        journalService.deleteById(id);
        return "redirect:/editor/journals";
    }


    @GetMapping("/addJournal")
    public String addJournal(@ModelAttribute("journal") Journal journal,
                             @RequestParam(name = "genres", required = false) List<String> genres,
                             Model model) {
        model.addAttribute("allGenres", genreService.getAll());
        model.addAttribute("genres", genres);
        model.addAttribute("journal", journal);
        return "editor/addJournal";
    }

    @PostMapping("/addJournal")
    public String createJournal(@ModelAttribute("journal") @Valid Journal journal, BindingResult bindingResult,
                             @RequestParam(name = "genres", required = false) List<String> genres,
                             Model model) {
        journal.setGenres(toSetOfGenre(genres));

        if(journal.getPublishingTerm() == null) {
            model.addAttribute("allGenres", genreService.getAll());
            model.addAttribute("genres", genres);
            model.addAttribute("journal", journal);
            model.addAttribute("publishingTermErr", "Publishing term has wrong format!");
        }

        if (bindingResult.hasFieldErrors("name") ||
                bindingResult.hasFieldErrors("publisher") ||
                bindingResult.hasFieldErrors("publishing") ||
                bindingResult.hasFieldErrors("country") ||
                bindingResult.hasFieldErrors("description")) {
            model.addAttribute("allGenres", genreService.getAll());
            model.addAttribute("genres", genres);
            model.addAttribute("journal", journal);
            return "editor/addJournal";
        }

        if (journalService.findByName(journal.getName()) != null) {
            model.addAttribute("allGenres", genreService.getAll());
            model.addAttribute("genres", genres);
            model.addAttribute("journal", journal);
            model.addAttribute("nameErr", "Name of journal is already exists!");
            return "editor/addJournal";
        }

        journalService.save(journal);

        return "redirect:/editor";
    }

    @GetMapping("/editJournal/{id}")
    public String editJournal(@PathVariable Integer id, Model model) {
        model.addAttribute("journal", journalService.findById(id));
        model.addAttribute("allGenres", genreService.getAll());
        return "editor/editJournal";
    }

    @PostMapping("/editJournal/{id}")
    public String updateJournal(@ModelAttribute(name = "journal") @Valid Journal journal, BindingResult bindingResult,
                             @RequestParam(name = "genres", required = false) List<String> genres,
                             Model model) {
        Journal oldJournal = journalService.findById(journal.getId());
        journal.setGenres(toSetOfGenre(genres));

        if(journal.getPublishingTerm() == null) {
            model.addAttribute("allGenres", genreService.getAll());
            model.addAttribute("genres", genres);
            model.addAttribute("journal", journal);
            model.addAttribute("publishingTermErr", "Publishing term has wrong format!");
            return "editor/editJournal";
        }

        if (bindingResult.hasFieldErrors("name") ||
                bindingResult.hasFieldErrors("publisher") ||
                bindingResult.hasFieldErrors("publishingTerm") ||
                bindingResult.hasFieldErrors("country") ||
                bindingResult.hasFieldErrors("description")) {
            model.addAttribute("allGenres", genreService.getAll());
            model.addAttribute("genres", genres);
            model.addAttribute("journal", journal);
            return "editor/editJournal";
        }

        if (journalService.findByName(journal.getName()) != null && !journal.getName().equals(oldJournal.getName())) {
            model.addAttribute("allGenres", genreService.getAll());
            model.addAttribute("genres", genres);
            model.addAttribute("journal", journal);
            model.addAttribute("nameErr", "Name of journal is already exists!");
            return "editor/editJournal";
        }

        journal.setJournalRatings(oldJournal.getJournalRatings());
        journal.setFavoriteUsers(oldJournal.getFavoriteUsers());
        journalService.save(journal);

        return "redirect:/editor/journals";
    }

    //------------------------------------------------Journal

}
