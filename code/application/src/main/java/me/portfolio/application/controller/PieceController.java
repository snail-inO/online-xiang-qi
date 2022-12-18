package me.portfolio.application.controller;

import me.portfolio.application.DAO.PieceDAO;
import me.portfolio.application.service.*;
import me.portfolio.library.entity.*;
import me.portfolio.library.exceptions.EntityNotFoundException;
import me.portfolio.library.exceptions.InvalidOperationException;
import me.portfolio.library.service.PieceStrategySelector;
import me.portfolio.library.util.GameStatusEnum;
import me.portfolio.library.util.PieceColorEnum;
import me.portfolio.library.util.UserStatusEnum;
import me.portfolio.log.aop.Logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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

//    @Logging
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePiece(@PathVariable String id, @RequestBody Piece piece, @RequestParam int mode1, @RequestParam int mode2) {
        Piece curPiece = pieceDAO.findById(id).orElseThrow(() -> new EntityNotFoundException(Piece.class, piece.getId()));
        List<Board> boards = curPiece.getBoards();
        Board board = boards.get(boards.size() - 1);
        Game game = board.getGame();
        LOGGER.info(board.getId());

        validateUserStatus(game, id);

        if (mode1 == 0) {
            Piece postPiece = PieceStrategySelector.SELECT_BY_TYPE(curPiece.getType()).move(board, curPiece, piece.getCol(),
                    piece.getRow());
            game = gameService.updateGame(board, curPiece, postPiece);
        } else {
            List<Piece> performedAction = AIServiceImpl.act(game, mode1, game.getUsers().get(PieceColorEnum.RED).getId().equals(AIServiceImpl.getUser().getId()) ? PieceColorEnum.BLACK : PieceColorEnum.RED);
            game = gameService.updateGame(board, performedAction.get(0), performedAction.get(1));
        }

        gameService.publishGameEvent(game);
        if (game.getStatus() == GameStatusEnum.END) {
            game.getUsers().values().forEach(user -> {
                user.setStatus(UserStatusEnum.ONLINE);
                userService.setUserStatus(user);
            });
            if (game.getWinner() != null) {
                userService.addWinCount(game.getWinner());
            }
        } else if (mode2 != 0 && isAI(game)) {
            List<Piece> performedAction = AIServiceImpl.act(game, mode2, null);
            game = gameService.updateGame(board, performedAction.get(0), performedAction.get(1));
        }
        gameService.publishGameEvent(game);

        return ResponseEntity.created(linkTo(methodOn(PieceController.class).updatePiece(id, piece, mode1, mode2)).withSelfRel()
                .toUri()).body(piece);
    }

    private boolean isAI(Game game) {
        AtomicBoolean res = new AtomicBoolean(false);
        game.getUsers().values().forEach(user -> {
            if (user.getId().equals(AIServiceImpl.getUser().getId())) {
                res.set(true);
            }
        });

        return res.get();
    }

    private void validateUserStatus(Game game, String pid) {
        try {
            game.getUsers().values().forEach(user -> {
                user.setStatus(UserStatusEnum.GAMING);
                userService.setUserStatus(user);
            });
        } catch (InvalidOperationException e) {
            User winner = game.getUsers().get(game.getTotalSteps() % 2 == 1 ? PieceColorEnum.RED : PieceColorEnum.BLACK);
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
