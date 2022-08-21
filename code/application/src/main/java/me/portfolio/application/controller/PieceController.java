package me.portfolio.application.controller;

import me.portfolio.application.DAO.PieceDAO;
import me.portfolio.application.service.GameService;
import me.portfolio.application.service.GameServiceImpl;
import me.portfolio.application.service.UserService;
import me.portfolio.application.service.UserServiceImpl;
import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Game;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.entity.User;
import me.portfolio.library.exceptions.EntityNotFoundException;
import me.portfolio.library.exceptions.InvalidOperationException;
import me.portfolio.library.service.PieceStrategySelector;
import me.portfolio.library.util.PieceColorEnum;
import me.portfolio.library.util.UserStatusEnum;
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
    private final static Logger LOGGER = LoggerFactory.getLogger(PieceController.class);
    private final PieceDAO pieceDAO;
    private final GameService gameService;
    private final UserService userService;

    public PieceController(PieceDAO pieceDAO, GameServiceImpl gameService, UserServiceImpl userService) {
        this.pieceDAO = pieceDAO;
        this.gameService = gameService;
        this.userService = userService;
    }

    @Logging
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePiece(@PathVariable String id, @RequestBody Piece piece) {

        Piece curPiece = pieceDAO.findById(id).orElseThrow(() -> new EntityNotFoundException(Piece.class, piece.getId()));
        List<Board> boards = curPiece.getBoards();
        Board board = boards.get(boards.size() - 1);
        Game game = board.getGame();
        LOGGER.info(board.getId());

        validateUserStatus(game, id);

        Piece postPiece = PieceStrategySelector.SELECT_BY_TYPE(curPiece.getType()).move(board, curPiece, piece.getCol(),
                piece.getRow());

        game = gameService.updateGame(board, curPiece, postPiece);
        if (game.getWinner() != null) {
            game.getUsers().values().forEach(user -> {
                user.setStatus(UserStatusEnum.ONLINE);
                userService.setUserStatus(user);
            });
            userService.addWinCount(game.getWinner());
        }
        gameService.publishGameEvent(game);

        return ResponseEntity.created(linkTo(methodOn(PieceController.class).updatePiece(id, piece)).withSelfRel()
                .toUri()).body(piece);
    }

    private void validateUserStatus(Game game, String pid) {
        try {
            game.getUsers().values().forEach(user -> {
                user.setStatus(UserStatusEnum.GAMING);
                userService.setUserStatus(user);
            });
        } catch (InvalidOperationException e) {
            PieceColorEnum loser = null;
            for (Map.Entry<PieceColorEnum, User> entry : game.getUsers().entrySet()) {
                loser = entry.getValue().getStatus() == UserStatusEnum.OFFLINE ? entry.getKey() : loser;
            }
            User winner = game.getUsers().get(loser == PieceColorEnum.BLACK ? PieceColorEnum.RED : PieceColorEnum.BLACK);
            gameService.endGame(game, winner);
            game.getUsers().values().forEach(user -> {
                if (user.getStatus() != UserStatusEnum.OFFLINE) {
                    user.setStatus(UserStatusEnum.ONLINE);
                    userService.setUserStatus(user);
                }
            });
            userService.addWinCount(winner);
            gameService.publishGameEvent(game);

            throw new InvalidOperationException(Piece.class, pid);
        }
    }
}
