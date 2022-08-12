package me.portfolio.application.controller;

import me.portfolio.application.DAO.BoardDAO;
import me.portfolio.application.DAO.PieceDAO;
import me.portfolio.application.service.GameService;
import me.portfolio.application.service.GameServiceImpl;
import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.exceptions.EntityNotFoundException;
import me.portfolio.library.service.PieceStrategySelector;
import me.portfolio.log.aop.Logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/pieces")
public class PieceController {
    private final PieceDAO pieceDAO;
    private final BoardDAO boardDAO;
    private final GameService gameService;

    private final static Logger LOGGER = LoggerFactory.getLogger(PieceController.class);

    public PieceController(PieceDAO pieceDAO, BoardDAO boardDAO, GameServiceImpl gameService) {
        this.pieceDAO = pieceDAO;
        this.boardDAO = boardDAO;
        this.gameService = gameService;
    }

    @Logging
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePiece(@PathVariable String id, @RequestBody Piece piece) {
        Piece curPiece = pieceDAO.findById(id).orElseThrow(() -> new EntityNotFoundException(Piece.class, piece.getId()));
        List<Board> boards = curPiece.getBoards();
        Board board = boards.get(boards.size() - 1);
        LOGGER.info(board.getId());
        Board finalBoard = board;
        board = boardDAO.findById(board.getId()).orElseThrow(() -> new EntityNotFoundException(Board.class, finalBoard.getId()));
        Map<Integer, Piece> pieceMap = board.getPieces();
        int count = 0;
        for (Integer key : pieceMap.keySet()) {
            String pid = pieceMap.get(key).getId();
            pieceMap.replace(key, pieceDAO.findById(pid).orElseThrow(() -> new EntityNotFoundException(Piece.class, pid)));
        }
        LOGGER.info("count {}", count);
        curPiece.setStrategy(PieceStrategySelector.SELECT_BY_TYPE(curPiece.getType()));
        Piece postPiece = curPiece.getStrategy().move(board, curPiece, piece.getCol(), piece.getRow());

        if (postPiece.getCol() == piece.getCol() && postPiece.getRow() == piece.getRow()) {
            gameService.updateGame(board, curPiece, postPiece);
            return ResponseEntity.created(linkTo(methodOn(PieceController.class).updatePiece(id, piece)).withSelfRel().toUri()).body(piece);
        }

        return ResponseEntity.badRequest().body("update piece failure, piece id: " + id);
    }
}
