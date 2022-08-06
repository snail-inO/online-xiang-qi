package me.portfolio.application.service;

import me.portfolio.application.DAO.BoardDAO;
import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.entity.PieceTypeEnum;
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
    public Board initBoard() {
        Map<String, Piece> pieceMap = new HashMap<>();
        Arrays.stream(PieceTypeEnum.values()).sequential().forEach(pieceType -> {
            pieceMap.putAll(pieceService.initPiece(pieceType));
        });

        return boardDAO.save(new Board(null, 0L, pieceMap));
    }

}
