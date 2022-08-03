package me.portfolio.application.controller;

import me.portfolio.application.service.UserService;
import me.portfolio.application.service.UserServiceImpl;
import me.portfolio.library.entity.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;

import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UserServiceController {

    private UserService userService;

    public UserServiceController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/userservice")
    public Mono<User> newUser() {
        return Mono.fromCallable(userService::newUser);
    }
}
