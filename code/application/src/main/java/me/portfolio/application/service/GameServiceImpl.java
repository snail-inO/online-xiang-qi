package me.portfolio.application.service;

import me.portfolio.application.DAO.GameDAO;
import me.portfolio.application.websocket.EntityEvent;
import me.portfolio.library.entity.*;
import me.portfolio.library.util.GameStatusEnum;
import me.portfolio.library.util.GetPieceIndex;
import me.portfolio.library.util.PieceColorEnum;
import me.portfolio.log.aop.Logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    private final GameDAO gameDAO;
    private final BoardService boardService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final static Logger LOGGER = LoggerFactory.getLogger(GameServiceImpl.class);

    public GameServiceImpl(GameDAO gameDAO, BoardServiceImpl boardService, ApplicationEventPublisher applicationEventPublisher) {
        this.gameDAO = gameDAO;
        this.boardService = boardService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Game initGame(Collection<User> users) {
        AtomicInteger random = new AtomicInteger((int) Math.round(Math.random()));
        Map<PieceColorEnum, User> userColor = new HashMap<>();
        Game initGame = new Game();
        Game game = null;
        initGame = gameDAO.save(initGame);
        try {
            users.stream().forEach(user -> {
                userColor.put(PieceColorEnum.values()[random.getAndSet((random.get() + 1) % 2)],user);
            });
            game = new Game(initGame.getId(), GameStatusEnum.IN_PROGRESS, 0, userColor,
                    Collections.singletonList(boardService.initBoard(initGame)).stream().collect(Collectors.toList()));
        } catch (Exception e) {
            gameDAO.delete(initGame);
            throw e;
        }

        game = gameDAO.save(game);
        publishGameEvent(game);
        return game;
    }

    @Override
    public void updateGame(Board board, Piece prePiece, Piece curPiece) {
        LOGGER.info("Update game: {}", prePiece);
        Game curGame = board.getGame();
        Board newBoard = boardService.updateBoard(board, prePiece, curPiece);

        curGame.getBoards().add(newBoard);
        curGame.setTotalSteps(curGame.getTotalSteps() + 1);

        publishGameEvent(gameDAO.save(curGame));
    }

    private void publishGameEvent(Game game) {
        applicationEventPublisher.publishEvent(new EntityEvent<>(this, game));
    }
}
