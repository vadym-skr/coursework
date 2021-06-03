package com.example.demo.controllers;

import com.example.demo.services.*;
import org.springframework.stereotype.Controller;

@Controller
public class AllServicesController {

    protected BookService bookService;
    protected JournalService journalService;
    protected GenreService genreService;
    protected UserService userService;
    protected BookRatingService bookRatingService;
    protected JournalRatingService journalRatingService;
    protected RoleService roleService;
    protected MailSender mailSender;

    public AllServicesController(BookService bookService, JournalService journalService ,GenreService genreService, UserService userService, BookRatingService bookRatingService, JournalRatingService journalRatingService, RoleService roleService, MailSender mailSender) {
        this.bookService = bookService;
        this.genreService = genreService;
        this.userService = userService;
        this.bookRatingService = bookRatingService;
        this.journalRatingService = journalRatingService;
        this.roleService = roleService;
        this.mailSender = mailSender;
        this.journalService = journalService;
    }
}
