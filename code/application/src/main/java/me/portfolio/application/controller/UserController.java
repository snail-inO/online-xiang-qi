package me.portfolio.application.controller;

import me.portfolio.application.DAO.UserDAO;
import me.portfolio.application.assembler.UserModelAssembler;
import me.portfolio.application.service.UserService;
import me.portfolio.application.service.UserServiceImpl;
import me.portfolio.library.entity.User;
import me.portfolio.library.entity.UserStatusEnum;
import me.portfolio.library.exceptions.EntityNotFoundException;
import me.portfolio.log.aop.Logging;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserDAO userDAO;
    private final UserModelAssembler assembler;
    private final UserService userService;

    public UserController(UserDAO userDAO, UserModelAssembler assembler, UserServiceImpl userService) {
        this.userDAO = userDAO;
        this.assembler = assembler;
        this.userService = userService;
    }

    @GetMapping
    public CollectionModel<EntityModel<User>> retrieveUsers() {
        List<EntityModel<User>> users = userDAO.findAll().stream()
                .map(assembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(users, linkTo(methodOn(UserController.class).retrieveUsers()).withSelfRel());
    }

    @GetMapping("/{uid}")
    public EntityModel<User> retrieveUser(@PathVariable(name = "uid") String uid) {
        User user = userDAO.findById(uid).orElseThrow(() -> new RuntimeException());

        return assembler.toModel(user);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        EntityModel<User> entityModel = assembler.toModel(userService.setUserStatus(user));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Logging
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        user.setStatus(UserStatusEnum.OFFLINE);
        user = userDAO.save(user);
        user.setStatus(UserStatusEnum.ONLINE);
        EntityModel<User> entityModel = assembler.toModel(userService.setUserStatus(user));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "uid") String uid) {
        User user = userDAO.findById(uid).orElseThrow(() -> new EntityNotFoundException(User.class, uid));
        user.setStatus(UserStatusEnum.OFFLINE);
        userService.setUserStatus(user);
        userDAO.deleteById(uid);

        return ResponseEntity.noContent().build();
    }
}
