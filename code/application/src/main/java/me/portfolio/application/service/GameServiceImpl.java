package me.portfolio.application.service;

import me.portfolio.application.DAO.GameDAO;
import me.portfolio.application.websocket.EntityEvent;
import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Game;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.entity.User;
import me.portfolio.library.exceptions.InvalidOperationException;
import me.portfolio.library.util.GameStatusEnum;
import me.portfolio.library.util.PieceColorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    private final static Logger LOGGER = LoggerFactory.getLogger(GameServiceImpl.class);
    private final GameDAO gameDAO;
    private final BoardService boardService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public GameServiceImpl(GameDAO gameDAO, BoardServiceImpl boardService, ApplicationEventPublisher applicationEventPublisher) {
        this.gameDAO = gameDAO;
        this.boardService = boardService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Game initGame(Collection<User> users, int size) {
        AtomicInteger random = new AtomicInteger((int) Math.round(Math.random()));
        Map<PieceColorEnum, User> userColor = new HashMap<>();
        Game initGame = new Game();
        Game game = null;
        initGame = gameDAO.save(initGame);
        try {
            users.stream().forEach(user -> {
                userColor.put(PieceColorEnum.values()[random.getAndSet((random.get() + 1) % 2)], user);
            });
//            initGame.setUsers(userColor);
            game = new Game(initGame.getId(), GameStatusEnum.IN_PROGRESS, 0, userColor,
                    Collections.singletonList(boardService.initBoard(initGame, size)).stream().collect(Collectors.toList()),
                    null);
        } catch (Exception e) {
            gameDAO.delete(initGame);
            throw e;
        }

        game = gameDAO.save(game);
        publishGameEvent(game);
        return game;
    }

    @Override
    public Game updateGame(Board board, Piece prePiece, Piece curPiece) {
        LOGGER.info("Update game: {}", prePiece);
        boolean res = false;
        Game curGame = board.getGame();
        if (!validate(curGame)) {
            throw new InvalidOperationException(Game.class, curGame.getId());
        }

        Board newBoard = boardService.updateBoard(board, prePiece, curPiece);

        String gameId = curGame.getId();
        curGame = gameDAO.findById(gameId).orElseThrow(() -> new InvalidOperationException(Game.class, gameId));
        curGame.getBoards().add(newBoard);
        curGame.setTotalSteps(curGame.getTotalSteps() + 1);

        return gameDAO.save(curGame);
    }

    @Override
    public Game endGame(Game game, User winner) {
        game.setWinner(winner);
        game.setStatus(GameStatusEnum.END);
        return gameDAO.save(game);
    }

    private boolean validate(Game game) {
        if (game.getStatus() == GameStatusEnum.END) {
            return false;
        }
//       AtomicBoolean res = new AtomicBoolean(true);
//       game.getUsers().values().forEach(user -> {
//           if (user.getStatus() != UserStatusEnum.GAMING) {
//               res.set(false);
//           }
//       });

//       return res.get();
        return true;
    }

    public void publishGameEvent(Game game) {
        applicationEventPublisher.publishEvent(new EntityEvent<>(this, game));
    }
}
