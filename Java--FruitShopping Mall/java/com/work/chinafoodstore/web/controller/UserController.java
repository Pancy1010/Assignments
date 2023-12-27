package com.work.chinafoodstore.web.controller;

import com.work.chinafoodstore.service.UserService;
import com.work.chinafoodstore.web.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * The user logs in and registers the api controller
 */
@Controller
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String resister() {
        return "register";
    }

    @PostMapping("/register/save")
    public String registerSave(@RequestBody UserDto userDto) {
        userService.saveUser(userDto);
        return "redirect:/login";
    }
}
