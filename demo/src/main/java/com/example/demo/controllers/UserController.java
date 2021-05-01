package com.example.demo.controllers;

import com.example.demo.entity.MyUserDetails;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.services.RoleService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.Transient;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Set;

// тут буде доступ в усіх людей(не зарієстрованих також)
// реєстрація ввід пароля і тд.
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String ShowUser(Model model) {
        // я хз чого, просто тут странно робить перевірка isAuthentication(), вона завжди false вертає, тому я так зробив
        // якщо user не ввійшов то .getPrincipal() поверне string(но це не точно)
        // якщо user ввійшов то .getPrincipal() поверне MyUserDetails(це точно)
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user.getClass().equals(MyUserDetails.class)) {
            model.addAttribute("user", userService.getCurrentUser());
            model.addAttribute("roles", roleService.getAll());
        }
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
    public String editUserPassword(Model model) {
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


    //@ModelAttribute -  При використанні в якості аргументу методу він вказує, що аргумент повинен бути отриманий з моделі. Якщо він відсутній, його слід спочатку створити, а потім додати в модель, і після того, як він присутній в моделі, поля аргументів повинні бути заповнені з усіх параметрів запиту, які мають співпадаючі імена.
    @GetMapping("/registration")
    public String registrationUser(@ModelAttribute("user") User user) {
        return "user/registration";
    }
    @PostMapping("/registration")
    public String createUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult, Model model) {
        // якщо імя вже присутнє в бд то переходим назад на сторінку реєстрації і виводим помилку
        if(userService.findUserByUsername(user.getUsername()) != null) {
            model.addAttribute("usernameErr", "This username already exists");
            return "user/registration";
        }
        if (bindingResult.hasErrors())
            return "user/registration";
        // кодуєм пароль(чого це сетером не зробити? - то го що при повернені назад на сторінку(якщо якась помилка при вводі) повернеться вже зашифрований пароль)
        user.encodePassword(user.getPassword());
        // призначаєм ролі і чи вкл акаунт, тому що це юзер не буде бачити
        user.setEnabled(true);
        user.setRoles(Collections.singleton(roleService.getRoleByName("USER")));
        userService.save(user);
        return "redirect:/user";
    }

}
