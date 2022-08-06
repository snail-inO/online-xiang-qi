package me.portfolio.application.service;

import me.portfolio.application.DAO.GameDAO;
import me.portfolio.library.entity.*;
import me.portfolio.log.aop.Logging;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    private final GameDAO gameDAO;
    private final BoardService boardService;

    public GameServiceImpl(GameDAO gameDAO, BoardServiceImpl boardService) {
        this.gameDAO = gameDAO;
        this.boardService = boardService;
    }

    @Logging
    @Override
    public Game initGame(Collection<User> users) {
        AtomicInteger random = new AtomicInteger((int) Math.round(Math.random()));
        Map<PieceColorEnum, User> userColor = new HashMap<>();
        users.stream().forEach(user -> {
            userColor.put(PieceColorEnum.values()[random.getAndSet((random.get() + 1) % 2)],user);
        });
        Game game = new Game(null, GameStatusEnum.IN_PROGRESS, 0, userColor,
                Collections.singletonList(boardService.initBoard()).stream()
                        .collect(Collectors.toMap(board -> String.valueOf(board.getStep()), board -> board)));

        return gameDAO.save(game);
    }
}
