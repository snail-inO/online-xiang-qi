package me.portfolio.application.controller;

import me.portfolio.application.DAO.UserDAO;
import me.portfolio.application.assembler.UserModelAssembler;
import me.portfolio.application.service.GameService;
import me.portfolio.application.service.GameServiceImpl;
import me.portfolio.application.service.UserService;
import me.portfolio.application.service.UserServiceImpl;
import me.portfolio.library.entity.User;
import me.portfolio.library.util.UserStatusEnum;
import me.portfolio.library.exceptions.EntityNotFoundException;
import me.portfolio.library.util.MatchingQueue;
import me.portfolio.log.aop.Logging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserDAO userDAO;
    private final UserModelAssembler assembler;
    private final UserService userService;
    private final GameService gameService;

    public UserController(UserDAO userDAO, UserModelAssembler assembler, UserServiceImpl userService, GameServiceImpl gameService) {
        this.userDAO = userDAO;
        this.assembler = assembler;
        this.userService = userService;
        this.gameService = gameService;
    }

    @GetMapping
    public PagedModel<EntityModel<User>> retrieveUsers(@PageableDefault Pageable pageable) {
        Page<User> userPage = userDAO.findAll(pageable);
        Collection<EntityModel<User>> userModels = userPage.getContent().stream()
                .map(assembler::toModel).collect(Collectors.toList());
        return PagedModel.of(userModels, new PagedModel.PageMetadata(
                userPage.getSize(), userPage.getNumber(), userPage.getTotalElements(), userPage.getTotalPages()),
                linkTo(methodOn(UserController.class).retrieveUsers(pageable)).withSelfRel());
    }

    @GetMapping("/{uid}")
    public EntityModel<User> retrieveUser(@PathVariable(name = "uid") String uid) {
        User user = userDAO.findById(uid).orElseThrow(() -> new RuntimeException());

        return assembler.toModel(user);
    }

    @Logging
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        User updatedUser = userService.setUserStatus(user);
        tryStartingGame(updatedUser);
        EntityModel<User> entityModel = assembler.toModel(updatedUser);

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

    private void tryStartingGame(final User user) {
        if (user.getStatus() == UserStatusEnum.MATCHING) {
            Queue<String> queue = MatchingQueue.getQueue();
            if (queue.size() > 1) {
                List<User> users = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    String uid = queue.poll();
                    users.add(userDAO.findById(uid).orElseThrow(() -> new EntityNotFoundException(User.class, uid)));
                }
                gameService.initGame(users);
                users.forEach(u -> {
                    u.setStatus(UserStatusEnum.GAMING);
                    userService.setUserStatus(u);
                });
            }

        }
    }
}
