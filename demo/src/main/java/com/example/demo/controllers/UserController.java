package com.example.demo.controllers;

import com.example.demo.entity.User;
import com.example.demo.services.MailSender;
import com.example.demo.services.RoleService;
import com.example.demo.services.UserService;
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
public class UserController {

    private final MailSender mailSender;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService, MailSender mailSender) {
        this.userService = userService;
        this.roleService = roleService;
        this.mailSender = mailSender;
    }

    @GetMapping
    public String ShowUser(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("roles", roleService.getAll());
        //"r.vitalik.skuratov@gmail.com"
        mailSender.send("vadim.skuratovskij@gmail.com", "Active code", "Hello user, ;)");
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
                             BindingResult bindingResult, Model model) {
        if(userService.findUserByUsername(user.getUsername()) != null) {
            model.addAttribute("usernameErr", "This username already exists");
            return "user/registration";
        }
        if (bindingResult.hasErrors())
            return "user/registration";

        userService.create(user,roleService.getRoleByName("USER"));

        return "redirect:/user";
    }

//    @GetMapping("/activate")
//    public String activateUser(@RequestParam("code") String oldPass,
//            Model model) {
//        //String code = UUID.randomUUID().toString();
//        model.addAttribute("code", code);
//        return "user/editPassword";
//    }
//    @GetMapping("/activate")
//    public String activateUser(@RequestParam("code") String oldPass,
//            Model model) {
//        String code = UUID.randomUUID().toString();
//        model.addAttribute("code", code);
//        return "redirect:/user";
//    }
}
