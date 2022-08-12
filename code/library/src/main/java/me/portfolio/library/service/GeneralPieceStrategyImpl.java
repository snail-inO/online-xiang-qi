package me.portfolio.library.service;

import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.util.BoundaryEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GeneralPieceStrategyImpl implements PieceStrategy {

    private final static Logger LOGGER = LoggerFactory.getLogger(GeneralPieceStrategyImpl.class);

    private static boolean SelfPieceCheck(Board board, Piece prePiece, int curCol, int curRow) {
        int index = 9 * curRow + curCol;


        return board.getPieces().containsKey(index) && board.getPieces().get(index).getColor().equals(prePiece.getColor());
    }

    private static Boolean ValidatePieceBoundary(Piece prePiece, int curCol, int curRow) {
//        int colDiff = Math.abs(curCol-prePiece.getCol());
//        int rowDiff = Math.abs(curRow-prePiece.getRow());
//        int min = Math.min(colDiff,rowDiff);
//        int max = Math.max(colDiff,rowDiff);
//        return min == prePiece.getType().getMin() && max <= prePiece.getType().getMax()
//                && max >= 1  &&
        return curRow >= BoundaryEnum.BOARD_BOUNDARY.getBottom() && curRow <= BoundaryEnum.BOARD_BOUNDARY.getTop()
                && curCol >= BoundaryEnum.BOARD_BOUNDARY.getLeft() && curCol <= BoundaryEnum.BOARD_BOUNDARY.getRight();
    }

    public Piece move(Board board, Piece prePiece, int curCol, int curRow) {
        Piece curPiece = new Piece(prePiece);

        int index = 9 * curRow + curCol;
        boolean res1 = ValidatePieceBoundary(prePiece, curCol, curRow);
        boolean res2 = !SelfPieceCheck(board, prePiece, curCol, curRow);
        boolean res3 = pieceMove(board, prePiece, curCol, curRow);
        LOGGER.info(String.valueOf(index));
        LOGGER.info(String.valueOf(res1));
        LOGGER.info(String.valueOf(res2));
        LOGGER.info(String.valueOf(res3));
        if (res1 && res2 && res3) {
            curPiece.setCol(curCol);
            curPiece.setRow(curRow);
            return curPiece;
        }
        return prePiece;
    }

    protected abstract boolean pieceMove(Board board, Piece prePiece, int curCol, int curRow);
}
