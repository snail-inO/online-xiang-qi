package me.portfolio.application.controller;

import me.portfolio.application.DAO.GameDAO;
import me.portfolio.library.entity.Game;
import me.portfolio.library.exceptions.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//@RestController
//@RequestMapping("/games")
//public class GameController {
//    private final GameDAO gameDAO;
//    private final GameModelAssembler assembler;
//
//    public GameController(GameDAO gameDAO, GameModelAssembler assembler) {
//        this.gameDAO = gameDAO;
//        this.assembler = assembler;
//    }
//
//    @GetMapping
//    public PagedModel<EntityModel<Game>> retrieveGames(@PageableDefault Pageable pageable) {
//        Page<Game> gamePage = gameDAO.findAll(pageable);
//        Collection<EntityModel<Game>> gameModels = gamePage.getContent().stream().map(assembler::toModel)
//                .collect(Collectors.toList());
//        return PagedModel.of(gameModels, new PagedModel.PageMetadata(
//                gamePage.getSize(), gamePage.getNumber(), gamePage.getTotalElements(), gamePage.getTotalPages()),
//                linkTo(methodOn(GameController.class).retrieveGames(pageable)).withSelfRel());
//    }
//
//    @GetMapping("/{id}")
//    public EntityModel<Game> retrieveGame(@PathVariable String id) {
//        return assembler.toModel(gameDAO.findById(id).orElseThrow(() -> new EntityNotFoundException(Game.class, id)));
//    }
//}
