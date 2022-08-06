package me.portfolio.application.assembler;

import me.portfolio.application.controller.UserController;

import me.portfolio.library.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User user) {
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).retrieveUser(user.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).retrieveUsers(Pageable.unpaged())).withRel("users"));
    }
}
