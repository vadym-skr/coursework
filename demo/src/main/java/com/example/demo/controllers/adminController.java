package com.example.demo.controllers;


import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.services.RoleService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Array;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class adminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public adminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/allUsers")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAll());
        return "admin/allUsers";
    }

    @GetMapping("/editUser/{id}")
    public String editUser(@PathVariable Integer id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("roles", roleService.getAll());
        User user = userService.getCurrentUser();
        return "admin/editUser";
    }

    @PostMapping("/editUser/{id}")
    public String userSave(@ModelAttribute("user") @Valid User newUser, BindingResult bindingResult,
                           @RequestParam(name = "roles", required = false) String[] roles,
                           Model model) {
        User user = userService.findUserById(newUser.getId());

        if(!newUser.getUsername().equals(user.getUsername()) && userService.findUserByUsername(newUser.getUsername()) != null) {
            model.addAttribute("usernameErr", "This username already exists");
            return "admin/editUser";
        }
        if (bindingResult.hasFieldErrors("username") || bindingResult.hasFieldErrors("email")) {
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
}
