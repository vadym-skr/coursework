package com.example.demo.controllers;

import com.example.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController extends AllServicesController {

    @Autowired
    public MainController(BookService bookService, JournalService journalService ,GenreService genreService, UserService userService, BookRatingService bookRatingService, JournalRatingService journalRatingService, RoleService roleService, MailSender mailSender) {
        super(bookService, journalService, genreService, userService, bookRatingService, journalRatingService, roleService, mailSender);
    }

    @GetMapping
    public String index() {
        return "main";
    }
}
