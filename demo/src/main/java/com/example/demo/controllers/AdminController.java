package com.example.demo.controllers;


import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.User;
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
@RequestMapping("/admin")
public class AdminController extends AllServicesController {

    @Autowired
    public AdminController(BookService bookService, JournalService journalService ,GenreService genreService, UserService userService, BookRatingService bookRatingService, JournalRatingService journalRatingService, RoleService roleService, MailSender mailSender) {
        super(bookService, journalService, genreService, userService, bookRatingService, journalRatingService, roleService, mailSender);
    }

    @GetMapping
    public String showAdmin() {
        return "admin/admin";
    }

    @GetMapping("/editUser/{id}")
    public String editUser(@PathVariable Integer id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("roles", roleService.getAll());
        return "admin/editUser";
    }

    @PostMapping("/editUser/{id}")
    public String userSave(@ModelAttribute("user") @Valid User newUser, BindingResult bindingResult,
                           @RequestParam(name = "roles", required = false) String[] roles,
                           Model model) {
        User user = userService.findUserById(newUser.getId());
        if(!newUser.getUsername().equals(user.getUsername()) && userService.findUserByUsername(newUser.getUsername()) != null) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getAll());
            model.addAttribute("usernameErr", "This username already exists");
            return "admin/editUser";
        }
        if (bindingResult.hasFieldErrors("username") || bindingResult.hasFieldErrors("email")) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getAll());
            return "admin/editUser";
        }

        if(roles != null) {
            List<Role> AllRoles = roleService.getAll();
            List<Role> newRolesForUser = new ArrayList<>();
            for (String roleName : roles) {
                for (Role role : AllRoles) {
                    if (role.getName().equals(roleName)) {
                        newRolesForUser.add(role);
                    }
                }
            }
            user.setRoles(new HashSet<>(newRolesForUser));
        }
        else {
            user.setRoles(new HashSet<>());
        }

        user.setUsername(newUser.getUsername());
        user.setEmail(newUser.getEmail());
        user.setEnabled(newUser.isEnabled());
        userService.save(user);

        return "redirect:/admin/allUsers";
    }

    @GetMapping("/allUsers")
    public String getAllUsers(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "enabled", required = false) Boolean enabled,
            @RequestParam(name = "role", required = false) String role,
            @RequestParam(name = "sort", required = false) String sort,
            Model model) {
        int size = 4;
        Role roleFromBD;

        if(page == null) { page = 1; }
        if(username == null || username.isBlank()) { username = ""; }
        if(email == null || email.isBlank()) { email = ""; }
        if(role == null || role.isEmpty()) {
            role = "";
            roleFromBD = roleService.getRoleByName("USER");
        }
        else {
            roleFromBD = roleService.getRoleByName(role);
        }
        if(sort == null) { sort = ""; }

        Page<User> users = userService.getForAllUsers(page - 1, size, sort, username, email, enabled, roleFromBD);

        model.addAttribute("users", users.getContent());
        model.addAttribute("maxPage", users.getTotalPages());
        model.addAttribute("page", page);
        model.addAttribute("username", username);
        model.addAttribute("email", email);
        model.addAttribute("enabled", enabled);
        model.addAttribute("role", role);
        model.addAttribute("sort", sort);

        return "admin/allUsers";
    }

    @Transactional
    @PostMapping("/allUsers")
    public String deleteUser(@RequestParam(name = "userId") Integer id){
        roleService.deleteUsersRolesForUser(id);
        bookRatingService.deleteBookRatingForUser(id);
        userService.deleteAllFavoriteBookForUser(id);
        userService.deleteUserById(id);
        return "redirect:/admin/allUsers";
    }

}
