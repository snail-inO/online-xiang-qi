package me.portfolio.application.controller;

import me.portfolio.application.service.UserService;
import me.portfolio.application.service.UserServiceImpl;
import me.portfolio.library.entity.User;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserServiceController {

    private UserService userService;

    public UserServiceController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/userservice")
    public User newUser() {
        return userService.newUser();
    }
}
