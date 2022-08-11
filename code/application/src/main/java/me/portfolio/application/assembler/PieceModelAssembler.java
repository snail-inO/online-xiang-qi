package me.portfolio.application.assembler;

import me.portfolio.application.controller.PieceController;
import me.portfolio.library.entity.Piece;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//public class PieceModelAssembler implements RepresentationModelAssembler<Piece, EntityModel<Piece>> {
//    @Override
//    public EntityModel<Piece> toModel(Piece piece) {
//        EntityModel.of(piece, linkTo(methodOn(PieceController.class).updatePiece()));
//    }
//}
