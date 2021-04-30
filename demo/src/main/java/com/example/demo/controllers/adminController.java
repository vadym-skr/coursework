package com.example.demo.controllers;


import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class adminController {

    private final UserService userService;
    @Autowired
    public adminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/allUsers")
    public String getAllUsers(Model model) {
        return "allUsers";
    }
}
