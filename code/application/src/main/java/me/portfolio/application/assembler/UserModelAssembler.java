package me.portfolio.application.assembler;

import me.portfolio.application.controller.UserController;

import me.portfolio.library.entity.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User user) {
        EntityModel<User> userModel = EntityModel.of(user,
                linkTo(methodOn(UserController.class).retrieveUser(user.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).retrieveUsers()).withRel("users"));

        return userModel;
    }
}
