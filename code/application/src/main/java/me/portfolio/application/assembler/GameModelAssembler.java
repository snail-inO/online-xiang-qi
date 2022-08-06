package me.portfolio.application.assembler;

import me.portfolio.library.entity.Game;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//@Component
//public class GameModelAssembler implements RepresentationModelAssembler<Game, EntityModel<Game>> {
//    @Override
//    public EntityModel<Game> toModel(Game game) {
//        return EntityModel.of(game, linkTo(methodOn(GameController.class).retrieveGame(game.getId())).withSelfRel(),
//                linkTo(methodOn(GameController.class).retrieveGames(Pageable.unpaged())).withRel("games"));
//    }
//}
