package com.example.demo.controllers;

import com.example.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AllServicesController {

    protected BookService bookService;
    protected GenreService genreService;
    protected UserService userService;
    protected RatingService ratingService;
    protected RoleService roleService;
    protected MailSender mailSender;

    public AllServicesController(BookService bookService, GenreService genreService, UserService userService, RatingService ratingService, RoleService roleService, MailSender mailSender) {
        this.bookService = bookService;
        this.genreService = genreService;
        this.userService = userService;
        this.ratingService = ratingService;
        this.roleService = roleService;
        this.mailSender = mailSender;
    }
}
