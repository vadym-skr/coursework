package com.example.demo.controllers.user;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.services.RoleService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.Access;
import javax.validation.Valid;
import java.util.Collections;

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
    public String user(Model model) {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // якщо user не зарієстрованний то user.class == string, інка повертає обєкта класу User
        if(user.getClass().equals(User.class))
            model.addAttribute("user", user);
        return "user/user";
    }

    //@ModelAttribute -  При використанні в якості аргументу методу він вказує, що аргумент повинен бути отриманий з моделі. Якщо він відсутній, його слід спочатку створити, а потім додати в модель, і після того, як він присутній в моделі, поля аргументів повинні бути заповнені з усіх параметрів запиту, які мають співпадаючі імена.
    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") User user) {
        return "user/registration";
    }
    @PostMapping("/registration")
    public String createUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult, Model model) {
        // якщо імя вже присутнє в бд то переходим назад на сторінку реєстрації і виводим помилку
        if(userService.getUserByUsername(user.getUsername()) != null) {
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
