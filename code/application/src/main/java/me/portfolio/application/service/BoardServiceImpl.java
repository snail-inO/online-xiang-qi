package me.portfolio.application.service;

import me.portfolio.application.DAO.BoardDAO;
import me.portfolio.application.DAO.GameDAO;
import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Game;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.exceptions.InvalidOperationException;
import me.portfolio.library.util.GameStatusEnum;
import me.portfolio.library.util.GetPieceIndex;
import me.portfolio.library.util.PieceColorEnum;
import me.portfolio.library.util.PieceTypeEnum;
import me.portfolio.log.aop.Logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BoardServiceImpl implements BoardService {
    private final static Logger LOGGER = LoggerFactory.getLogger(BoardServiceImpl.class);
    private final BoardDAO boardDAO;
    private final GameDAO gameDAO;
    private final PieceService pieceService;

    public BoardServiceImpl(BoardDAO boardDAO, GameDAO gameDAO, PieceService pieceService) {
        this.boardDAO = boardDAO;
        this.gameDAO = gameDAO;
        this.pieceService = pieceService;
    }

    @Logging
    @Override
    public Board initBoard(Game game, int size) {
        Map<Integer, Piece> pieceMap = new HashMap<>();
        Board initBoard = new Board();
        initBoard = boardDAO.save(initBoard);
        try {
            Board finalInitBoard = initBoard;
            PieceTypeEnum randomType;
            if (size > 1 && size < 7) {
                Set<PieceTypeEnum> record = new HashSet<>();
                pieceMap.putAll(pieceService.initPiece(finalInitBoard, PieceTypeEnum.SHUAI));
                randomType = getRandomPiece(true);
                record.add(randomType);
                pieceMap.putAll(pieceService.initPiece(finalInitBoard, randomType));
                size -= 2;
                while (size > 0) {
                    randomType = getRandomPiece(false);
                    if (record.contains(randomType)) {
                        continue;
                    }
                    record.add(randomType);
                    pieceMap.putAll(pieceService.initPiece(finalInitBoard, randomType));
                    size--;
                }
            } else {
                Arrays.stream(PieceTypeEnum.values()).sequential().forEach(pieceType -> {
                    pieceMap.putAll(pieceService.initPiece(finalInitBoard, pieceType));
                });
            }
        } catch (Exception e) {
            boardDAO.delete(initBoard);
            throw e;
        }

        return boardDAO.save(new Board(initBoard.getId(), 0L, pieceMap, game));
    }

    @Override
    public Board updateBoard(Board board, Piece prePiece, Piece curPiece) {
        LOGGER.info("Update board: {}", board);
//        Board newBoard = new Board(board);
//        newBoard.setId(null);
//        newBoard = boardDAO.save(newBoard);
        try {
            board.getPieces().remove(GetPieceIndex.getIndex(prePiece));

            int newIndex = GetPieceIndex.getIndex(curPiece);
            if (board.getPieces().containsKey(newIndex)) {
                Piece removedPiece = board.getPieces().get(newIndex);
                if (pieceService.updatePiece(removedPiece, false, board)) {
                    endGame(board, removedPiece);
                }
            }

            pieceService.updatePiece(curPiece, true, board);
            board.getPieces().remove(GetPieceIndex.getIndex(prePiece));
            board.getPieces().put(newIndex, curPiece);
            board.setStep(board.getStep() + 1);

            return boardDAO.save(board);
        } catch (Exception ex) {
            boardDAO.deleteById(board.getId());
            throw new InvalidOperationException(Board.class, board.getId());
        }
    }

    private PieceTypeEnum getRandomPiece(boolean attacker) {
        Random rnd = new Random();
        int offset = attacker ? 3 : 1;
        int bound = attacker ? 4 : 6;
        return PieceTypeEnum.values()[rnd.nextInt(bound) + offset];
    }

    private void endGame(Board board, Piece removedPiece) {
        Game game = board.getGame();
        game.setStatus(GameStatusEnum.END);
        game.setWinner(game.getUsers().get(
                removedPiece.getColor() == PieceColorEnum.RED ? PieceColorEnum.BLACK : PieceColorEnum.RED));

    }
}
