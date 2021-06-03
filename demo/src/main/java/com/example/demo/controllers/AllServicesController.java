package com.example.demo.controllers;

import com.example.demo.services.*;
import org.springframework.stereotype.Controller;

@Controller
public class AllServicesController {

    protected BookService bookService;
    protected GenreService genreService;
    protected UserService userService;
    protected BookRatingService bookRatingService;
    protected RoleService roleService;
    protected MailSender mailSender;

    public AllServicesController(BookService bookService, GenreService genreService, UserService userService, BookRatingService bookRatingService, RoleService roleService, MailSender mailSender) {
        this.bookService = bookService;
        this.genreService = genreService;
        this.userService = userService;
        this.bookRatingService = bookRatingService;
        this.roleService = roleService;
        this.mailSender = mailSender;
    }
}
