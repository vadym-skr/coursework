package com.example.demo.controllers;

import com.example.demo.entity.User;
import com.example.demo.services.RoleService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class allUsersAdminController {

    private final UserService userService;
    private final RoleService roleService;
    @Autowired
    public allUsersAdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
    @GetMapping("/allUsers")
    public String getAllUsers(
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "enabled", required = false) Boolean enabled,
            @RequestParam(name = "sort", required = false) String sort,
            Model model) {
        int amount = 3;
        if(username == null || username.isBlank()) {
            username = "";
        }
        if(email == null || email.isBlank()) {
            email = "";
        }
        if(page == null) {
            page = 1;
        }

        boolean enabledOne = true;
        boolean enabledTwo = false;
        if(enabled != null) {
            if(enabled) {
                enabledOne = true;
                enabledTwo = true;
                enabled = true;
            }
            else{
                enabledOne = false;
                enabledTwo = false;
                enabled = false;
            }
        }
        List<User> users = userService.findUsersByAllFields(username, email, enabledOne, enabledTwo);
        if(sort != null) {
            if(sort.equals("id up"))
                users = users.stream().sorted(Comparator.comparingInt(User::getId)).collect(Collectors.toList());
            if(sort.equals("id down"))
                users = users.stream().sorted(Comparator.comparingInt(User::getId).reversed()).collect(Collectors.toList());
        }
            model.addAttribute("users", users.stream()
                    .limit((long) amount * page)
                    .collect(Collectors.toList()));
        model.addAttribute("maxPage", ((double) users.size()) / amount);

        model.addAttribute("page", page);
        model.addAttribute("username", username);
        model.addAttribute("email", email);
        model.addAttribute("enabled", enabled);
        model.addAttribute("sort", sort);
        return "admin/allUsers";
    }

}
