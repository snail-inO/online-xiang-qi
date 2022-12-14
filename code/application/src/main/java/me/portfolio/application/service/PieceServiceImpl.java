package me.portfolio.application.service;

import me.portfolio.application.DAO.PieceDAO;
import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.util.PieceColorEnum;
import me.portfolio.library.util.PieceTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class PieceServiceImpl implements PieceService {
    private final static int BOARD_WIDTH = 9;
    private final static int BOARD_LENGTH = 10;
    private final static Logger LOGGER = LoggerFactory.getLogger(PieceServiceImpl.class);
    private final PieceDAO pieceDAO;

    public PieceServiceImpl(PieceDAO pieceDAO) {
        this.pieceDAO = pieceDAO;
    }

    //    @Logging
    @Override
    public Map<Integer, Piece> initPiece(Board board, PieceTypeEnum type) {
        Map<Integer, Piece> pieceMap = new HashMap<>();
        pieceMap.putAll(generatePieces(board, type, 0, true));
        if (type != PieceTypeEnum.SHUAI) {
            pieceMap.putAll(generatePieces(board, type, 0, false));
        }

        if (type == PieceTypeEnum.BING) {
            pieceMap.putAll(generatePieces(board, type, 2, true));
            pieceMap.putAll(generatePieces(board, type, 2, false));
            pieceMap.putAll(generatePieces(board, type, 4, true));
        }

        return pieceMap;
    }

    @Override
    public boolean updatePiece(Piece piece, boolean alive, Board board) {
        LOGGER.info("Update {}, alive {}", piece, alive);
        piece.getBoards().add(board);
        piece.setAlive(alive);
        if (!alive) {
            piece.setCol(-1);
            piece.setRow(-1);
            if (piece.getType() == PieceTypeEnum.SHUAI) {
                return true;
            }
        }
        pieceDAO.save(piece);
        return false;
    }

    private Map<Integer, Piece> generatePieces(Board board, PieceTypeEnum type, int step, boolean left) {
        Map<Integer, Piece> pieceMap = new HashMap<>();
        int flag = left ? 0 : 1;

        Piece pieceRL = new Piece(null, type,
                PieceColorEnum.RED, type.getInitRow(),
                BOARD_WIDTH * flag + (left ? 1 : -1) * (type.getInitCol() + step) - 1 * flag, true,
                Collections.singletonList(board));

        Piece pieceBL = new Piece(pieceRL);
        pieceBL.setColor(PieceColorEnum.BLACK);
        pieceBL.setRow(BOARD_LENGTH - type.getInitRow() - 1);

        pieceRL = pieceDAO.save(pieceRL);
        pieceBL = pieceDAO.save(pieceBL);
        pieceMap.put(pieceRL.getRow() * 9 + pieceRL.getCol(), pieceRL);
        pieceMap.put(pieceBL.getRow() * 9 + pieceBL.getCol(), pieceBL);

        return pieceMap;
    }

}
