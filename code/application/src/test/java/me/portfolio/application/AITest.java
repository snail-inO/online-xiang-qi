package me.portfolio.application;

import me.portfolio.application.DAO.PieceDAO;
import me.portfolio.application.DAO.TensorDAO;
import me.portfolio.application.DAO.UserDAO;
import me.portfolio.application.DAO.test.MetadataDAO;
import me.portfolio.application.service.AIServiceImpl;
import me.portfolio.application.service.GameService;
import me.portfolio.application.service.GameServiceImpl;
import me.portfolio.library.entity.*;
import me.portfolio.library.entity.test.Metadata;
import me.portfolio.library.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:user_test.properties")
public class AITest {
    @Autowired
    private GameService gameService;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private PieceDAO pieceDAO;
    @Autowired
    private MetadataDAO metadataDAO;
    @Autowired
    private TensorDAO tensorDAO;

    @Test
    public void testGame() {
        List<User> users = new ArrayList<>();
        User baseAI = new User();
        baseAI.setName("baseAi");
        User ai = AIServiceImpl.getUser();
        baseAI.setStatus(UserStatusEnum.GAMING);
        ai.setStatus(UserStatusEnum.GAMING);
        userDAO.save(baseAI);
        userDAO.save(ai);
        users.add(baseAI);
        users.add(ai);
        for (int size = 7; size < 8; size++) {
            for (int i = 0; i < 100; i++) {
                Metadata metadata = new Metadata();
                Game game = gameService.initGame(users, size);
                metadata.setSize(size);
                metadata.setId(game.getId());
                if (game.getUsers().get(PieceColorEnum.RED).getId().equals(ai.getId())) {
                    Piece curPiece = pieceDAO.findById(game.latestBoard().getPieces().values().iterator().next().getId()).orElseThrow(() -> new RuntimeException());
                    List<Board> boards = curPiece.getBoards();
                    Board board = boards.get(boards.size() - 1);
                    game = board.getGame();
                    List<Piece> performedAction = AIServiceImpl.act(game, 3, null);
                    game = gameService.updateGame(board, performedAction.get(0), performedAction.get(1));
                }
                while (game.getStatus() != GameStatusEnum.END) {
                    Piece curPiece = pieceDAO.findById(game.latestBoard().getPieces().values().iterator().next().getId()).orElseThrow(() -> new RuntimeException());
                    List<Board> boards = curPiece.getBoards();
                    Board board = boards.get(boards.size() - 1);
                    game = board.getGame();
                    List<Piece> performedAction = AIServiceImpl.act(game, 2, game.getUsers().get(PieceColorEnum.RED).getId().equals(AIServiceImpl.getUser().getId()) ? PieceColorEnum.BLACK : PieceColorEnum.RED);
                    game = gameService.updateGame(game.latestBoard(), performedAction.get(0), performedAction.get(1));
                    if (game.getStatus() == GameStatusEnum.END) {
                        break;
                    }
                    performedAction = AIServiceImpl.act(game, 3, null);
                    game = gameService.updateGame(game.latestBoard(), performedAction.get(0), performedAction.get(1));
                }
                System.out.println(i);
                metadata.setNodeCount(AIServiceImpl.getNodeCount());
                metadata.setTreeScore(new State(game.latestBoard()).getUtility() * (AIServiceImpl.getColor(game).equals(PieceColorEnum.RED)? 1 : -1));
                metadataDAO.save(metadata);
                AIServiceImpl.clearStates();
            }
        }
    }

    @Test
    public void generateTrainData() {
        List<User> users = new ArrayList<>();
        User baseAI = new User();
        baseAI.setName("baseAi");
        User ai = AIServiceImpl.getUser();
        baseAI.setStatus(UserStatusEnum.GAMING);
        ai.setStatus(UserStatusEnum.GAMING);
        userDAO.save(baseAI);
        userDAO.save(ai);
        users.add(baseAI);
        users.add(ai);
            for (int i = 0; i < 500; i++) {
                List<Tensor> tensors = new ArrayList<>();
                Game game = gameService.initGame(users, 5);
                int count = 20;
                if (game.getUsers().get(PieceColorEnum.RED).getId().equals(ai.getId())) {
                    Piece curPiece = pieceDAO.findById(game.latestBoard().getPieces().values().iterator().next().getId()).orElseThrow(() -> new RuntimeException());
                    List<Board> boards = curPiece.getBoards();
                    Board board = boards.get(boards.size() - 1);
                    game = board.getGame();
                    List<Piece> performedAction = AIServiceImpl.act(game, 2, null);
                    game = gameService.updateGame(board, performedAction.get(0), performedAction.get(1));
                    count--;
                }
                while (game.getStatus() != GameStatusEnum.END) {
                    Piece curPiece = pieceDAO.findById(game.latestBoard().getPieces().values().iterator().next().getId()).orElseThrow(() -> new RuntimeException());
                    List<Board> boards = curPiece.getBoards();
                    Board board = boards.get(boards.size() - 1);
                    game = board.getGame();
                    List<Piece> performedAction = AIServiceImpl.act(game, 2, game.getUsers().get(PieceColorEnum.RED).getId().equals(AIServiceImpl.getUser().getId()) ? PieceColorEnum.BLACK : PieceColorEnum.RED);
                    game = gameService.updateGame(game.latestBoard(), performedAction.get(0), performedAction.get(1));
                    count--;
                    if (game.getStatus() == GameStatusEnum.END) {
                        break;
                    }
                    performedAction = AIServiceImpl.act(game, count <= 0? 1 : 2, null);
                    game = gameService.updateGame(game.latestBoard(), performedAction.get(0), performedAction.get(1));
                    if (count <= 0) {
                        tensors.add(AIHelper.toTensor(new State(game.latestBoard())));
                    }
                    count--;
                }
                Float score = new State(game.latestBoard()).getUtility() * (AIServiceImpl.getColor(game).equals(PieceColorEnum.RED)? 1 : -1);
                tensors.forEach(tensor -> tensor.setScore(score));
                tensorDAO.saveAll(tensors);
                System.out.println(i);
                AIServiceImpl.clearStates();
            }
    }
}
