package com.example.demo.controllers;

import com.example.demo.entity.user.User;
import com.example.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController extends AllServicesController {

    @Autowired
    public UserController(BookService bookService, GenreService genreService, UserService userService, RatingService ratingService, RoleService roleService, MailSender mailSender) {
        super(bookService, genreService, userService, ratingService, roleService, mailSender);
    }

    @GetMapping
    public String ShowUser(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("roles", roleService.getAll());
        return "user/user";
    }

    @GetMapping("/edit")
    public String editUser(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "user/edit";
    }
    @PostMapping("/edit")
    public String updateUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult, Model model) {
        User currentUser = userService.getCurrentUser();
        if(!currentUser.getUsername().equals(user.getUsername()) &&
                userService.findUserByUsername(user.getUsername()) != null) {
            model.addAttribute("usernameErr", "This username already exists");
            return "user/edit";
        }
        if (bindingResult.hasFieldErrors("username") || bindingResult.hasFieldErrors("email"))
            return "user/edit";
        currentUser.setUsername(user.getUsername());
        currentUser.setEmail(user.getEmail());
        userService.saveAndUpdateCurrentUser(currentUser);
        return "redirect:/user";
    }

    @GetMapping("/editPassword")
    public String editUserPassword() {
        return "user/editPassword";
    }
    @PostMapping("/editPassword")
    public String updateUserPassword(@RequestParam("oldPass") String oldPass,
                                     @RequestParam("newPass") String newPass,
                                     Model model) {
        User currentUser = userService.getCurrentUser();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(oldPass, currentUser.getPassword())) {
            model.addAttribute("wrongOldPass", "Password does not match!");
            return "user/editPassword";
        }
        if(encoder.matches(newPass, currentUser.getPassword())) {
            model.addAttribute("wrongNewPass", "New Password matches the old password!");
            return "user/editPassword";
        }
        if(newPass.length() < 4) {
            model.addAttribute("wrongNewPass", "The length of the new password is less than 4 characters!");
            return "user/editPassword";
        }
        currentUser.setPassword(encoder.encode(newPass));
        userService.saveAndUpdateCurrentUser(currentUser);
        return "redirect:/user";
    }

    @GetMapping("/registration")
    public String registrationUser(@ModelAttribute("user") User user) {
        return "user/registration";
    }
    @PostMapping("/registration")
    public String createUser(@ModelAttribute("user") @Valid User user,
                             @ModelAttribute("code") String code,
                             BindingResult bindingResult, Model model) {
        if(userService.findUserByUsername(user.getUsername()) != null) {
            model.addAttribute("usernameErr", "This username already exists");
            return "user/registration";
        }
        if (bindingResult.hasErrors())
            return "user/registration";
        if (user.getActivatedCode() == null) {
            String newCode = UUID.randomUUID().toString();
            user.setActivatedCode(newCode);
            model.addAttribute("user", user);
            System.out.println(newCode); //todo delete id
            mailSender.send(user.getEmail(), "Active code", "Hello " + user.getUsername() + ", it's your code:\n" + user.getActivatedCode());
            return "user/registration";
        }
        if (!user.getActivatedCode().equals(code)) {
            model.addAttribute("errCode", "It was a wrong code!");
            model.addAttribute("user", user);
            return "user/registration";
        }
        userService.create(user,roleService.getRoleByName("USER"));

        return "redirect:/user";
    }
}
