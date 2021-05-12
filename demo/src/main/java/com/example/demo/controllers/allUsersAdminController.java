package com.example.demo.controllers;

import com.example.demo.services.RoleService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "action", required = false) String action,
            Model model) {
        if(page == null) {
            page = 1;
            model.addAttribute("page", 1);
        }
        if(action != null)
        {
            System.err.println(action);
            if(action.equals("moveBack"))
                page -= 1;
            else if(action.equals("moveForward"))
                page += 1;
            model.addAttribute("page", page);
        }
//        if (model.containsAttribute("currentNum"))
//            currentNum = (int) model.getAttribute("currentNum");
//        else
//            model.addAttribute("currentNum", 0);
        int amount = 3;
        //int finalPage = page * amount - amount;
        model.addAttribute("users", userService.getAll().stream().limit((long) amount * page).collect(Collectors.toList()));
        return "admin/allUsers";
    }

}
