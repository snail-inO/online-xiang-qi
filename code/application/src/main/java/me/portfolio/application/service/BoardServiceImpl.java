package me.portfolio.application.service;

import me.portfolio.application.DAO.BoardDAO;
import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Game;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.exceptions.InvalidOperationException;
import me.portfolio.library.util.GetPieceIndex;
import me.portfolio.library.util.PieceTypeEnum;
import me.portfolio.log.aop.Logging;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class BoardServiceImpl implements BoardService {
    private final BoardDAO boardDAO;
    private final PieceService pieceService;
    public BoardServiceImpl(BoardDAO boardDAO, PieceService pieceService) {
        this.boardDAO = boardDAO;
        this.pieceService = pieceService;
    }

    @Logging
    @Override
    public Board initBoard(Game game) {
        Map<Integer, Piece> pieceMap = new HashMap<>();
        Board initBoard = new Board();
        initBoard = boardDAO.save(initBoard);
        try {
            Board finalInitBoard = initBoard;
            Arrays.stream(PieceTypeEnum.values()).sequential().forEach(pieceType -> {
                pieceMap.putAll(pieceService.initPiece(finalInitBoard, pieceType));
            });
        } catch (Exception e) {
            boardDAO.delete(initBoard);
            throw e;
        }

        return boardDAO.save(new Board(initBoard.getId(), 0L, pieceMap, game));
    }

    @Logging
    @Override
    public Board updateBoard(Board board, Piece prePiece, Piece curPiece) {
        Board newBoard = new Board(board);
        newBoard.setId(null);
        newBoard = boardDAO.save(newBoard);
        try {
            newBoard.getPieces().remove(GetPieceIndex.getIndex(prePiece));

            int newIndex = GetPieceIndex.getIndex(curPiece);
            if (newBoard.getPieces().containsKey(newIndex)) {
                Piece removedPiece = newBoard.getPieces().get(newIndex);
                pieceService.updatePiece(removedPiece, false, newBoard);
            }

            pieceService.updatePiece(curPiece, true, newBoard);
            newBoard.getPieces().put(newIndex, curPiece);
            newBoard.setStep(newBoard.getStep() + 1);

            return boardDAO.save(newBoard);
        } catch (Exception ex) {
            boardDAO.deleteById(newBoard.getId());
            throw new InvalidOperationException(Board.class, board.getId());
        }
    }

}
