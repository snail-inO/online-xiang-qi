package me.portfolio.application.controller;

import me.portfolio.application.service.UserService;
import me.portfolio.application.service.UserServiceImpl;
import me.portfolio.library.entity.User;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


// Deprecated
//@RestController
//@RequestMapping("/user_service")
//public class UserServiceController {
//
//    private UserService userService;
//
//    public UserServiceController(UserServiceImpl userService) {
//        this.userService = userService;
//    }
//
//    @PostMapping("/matching")
//    @ResponseStatus(code = HttpStatus.CREATED, reason = "start matching")
//    public EntityModel<User> startMatching(@SessionAttribute String uid) {
//        userService.setMatching(uid, true);
//    }
//
//    @DeleteMapping("/matching")
//    @ResponseStatus(code = HttpStatus.NO_CONTENT, reason = "stop matching")
//    public EntityModel<User> stopMatching(@SessionAttribute String uid) {
//        userService.setMatching(uid, false);
//    }
//
//    @PostMapping("/online_status")
//    @ResponseStatus(code = HttpStatus.CREATED, reason = "set user online ")
//    public EntityModel<User> setUserOnline(@SessionAttribute String uid) {
//        return userService.setUserStatus(uid, true);
//    }
//
//    @DeleteMapping("/online_status")
//    @ResponseStatus(code = HttpStatus.NO_CONTENT, reason = "set user offline ")
//    public EntityModel<User> setUserOffline(@SessionAttribute String uid) {
//        return userService.setUserStatus(uid, false);
//    }
//
//}
