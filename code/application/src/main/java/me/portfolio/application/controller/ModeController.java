package me.portfolio.application.controller;

import me.portfolio.application.DAO.PieceDAO;
import me.portfolio.application.DAO.UserDAO;
import me.portfolio.application.service.AIServiceImpl;
import me.portfolio.application.service.GameService;
import me.portfolio.application.service.GameServiceImpl;
import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Game;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.entity.User;
import me.portfolio.library.util.GameModeEnum;
import me.portfolio.library.util.MatchingQueue;
import me.portfolio.library.util.PieceColorEnum;
import me.portfolio.library.util.UserStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mode")
public class ModeController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ModeController.class);
    private final UserDAO userDAO;
    private final PieceDAO pieceDAO;
    private final GameService gameService;

    public ModeController(UserDAO userDAO, PieceDAO pieceDAO, GameServiceImpl gameService) {
        this.userDAO = userDAO;
        this.pieceDAO = pieceDAO;
        this.gameService = gameService;
    }

    //    @Logging
    @PutMapping
    public ResponseEntity<?> setGameMode(@RequestParam String uid, @RequestParam int mode1, @RequestParam int mode2, @RequestParam int size) {
        if (mode2 > GameModeEnum.HUMAN.ordinal() && mode2 <= GameModeEnum.NN.ordinal()) {
            MatchingQueue.getQueue().poll();
            List<User> users = new ArrayList<>();
            User player = userDAO.findById(uid).orElseThrow(() -> new RuntimeException());
            User ai = AIServiceImpl.getUser();
            player.setStatus(UserStatusEnum.GAMING);
            ai.setStatus(UserStatusEnum.GAMING);
            userDAO.save(player);
            userDAO.save(ai);
            users.add(player);
            users.add(ai);
            Game game = gameService.initGame(users, size);
            AIServiceImpl.clearStates();
            if (game.getUsers().get(PieceColorEnum.RED).getId().equals(ai.getId())) {
                Piece curPiece = pieceDAO.findById(game.latestBoard().getPieces().values().iterator().next().getId()).orElseThrow(() -> new RuntimeException());
                List<Board> boards = curPiece.getBoards();
                Board board = boards.get(boards.size() - 1);
                game = board.getGame();
                List<Piece> performedAction = AIServiceImpl.act(game, mode2, null);
                game = gameService.updateGame(board, performedAction.get(0), performedAction.get(1));
            }
            gameService.publishGameEvent(game);
        }

        LOGGER.info("end set game mode");
        return ResponseEntity.ok().build();
    }
}
