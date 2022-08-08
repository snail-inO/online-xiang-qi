package me.portfolio.application.service;

import me.portfolio.application.DAO.GameDAO;
import me.portfolio.application.websocket.EntityEvent;
import me.portfolio.library.entity.*;
import me.portfolio.library.util.GameStatusEnum;
import me.portfolio.library.util.PieceColorEnum;
import me.portfolio.log.aop.Logging;
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

    public GameServiceImpl(GameDAO gameDAO, BoardServiceImpl boardService, ApplicationEventPublisher applicationEventPublisher) {
        this.gameDAO = gameDAO;
        this.boardService = boardService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Logging
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
        publishInitGameEvent(game);
        return game;
    }

    private void publishInitGameEvent(Game game) {
        applicationEventPublisher.publishEvent(new EntityEvent<>(this, game));
    }
}
