package me.portfolio.application.service;

import me.portfolio.application.DAO.PieceDAO;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.entity.PieceColorEnum;
import me.portfolio.library.entity.PieceTypeEnum;
import me.portfolio.library.service.PieceStrategySelector;
import me.portfolio.log.aop.Logging;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PieceServiceImpl implements PieceService{
    private final static int BOARD_WIDTH = 9;
    private final static int BOARD_LENGTH = 10;
    private final PieceDAO pieceDAO;

    public PieceServiceImpl(PieceDAO pieceDAO) {
        this.pieceDAO = pieceDAO;
    }

    @Logging
    @Override
    public Map<String, Piece> initPiece(PieceTypeEnum type) {
        Map<String, Piece> pieceMap = new HashMap<>();
        pieceMap.putAll(generatePieces(type, 0, true));
        if (type != PieceTypeEnum.SHUAI) {
            pieceMap.putAll(generatePieces(type, 0, false));
        }

        if (type == PieceTypeEnum.ZU) {
            pieceMap.putAll(generatePieces(type, 2, true));
            pieceMap.putAll(generatePieces(type, 2, false));
            pieceMap.putAll(generatePieces(type, 4, true));
        }

        return pieceMap;
    }

    private Map<String, Piece> generatePieces(PieceTypeEnum type, int step, boolean left) {
        Map<String, Piece> pieceMap = new HashMap<>();
        int flag = left? 0 : 1;

        Piece pieceRL = new Piece(null, type, PieceStrategySelector.SELECT_BY_TYPE(type),
                    PieceColorEnum.RED, type.getInitRow(),
                BOARD_WIDTH * flag + (left? 1 : -1) * (type.getInitCol() + step) - 1 * flag,  true);

        Piece pieceBL = new Piece(pieceRL);
        pieceBL.setColor(PieceColorEnum.BLACK);
        pieceBL.setRow(BOARD_LENGTH - type.getInitRow() - 1);

        pieceRL = pieceDAO.save(pieceRL);
        pieceBL = pieceDAO.save(pieceBL);
        pieceMap.put(pieceRL.getId(), pieceRL);
        pieceMap.put(pieceBL.getId(), pieceBL);

        return pieceMap;
    }

}
