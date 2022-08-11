package me.portfolio.library.service;

import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.util.BoundaryEnum;
import me.portfolio.library.util.GetPieceIndex;

public abstract class GeneralPieceStrategyImpl implements PieceStrategy {


    public Piece move(Board board, Piece prePiece, int curCol, int curRow) {
        Piece curPiece = new Piece(prePiece);


        if (ValidatePieceBoundary(prePiece, curCol, curRow) && !SelfPieceCheck(board, prePiece, curCol, curRow)
                && pieceMove(board, prePiece, curCol, curRow)
                ) {
            curPiece.setCol(curCol);
            curPiece.setRow(curRow);
            return curPiece;
        }
        return prePiece;
    }

    private static boolean SelfPieceCheck(Board board, Piece prePiece, int curCol, int curRow){
        int index = 9 * curRow + curCol-1;

        return board.getPieces().containsKey(index) && board.getPieces().get(index).getColor().equals(prePiece.getColor());
    }

    private static Boolean ValidatePieceBoundary(Piece prePiece, int curCol, int curRow) {
//        int colDiff = Math.abs(curCol-prePiece.getCol());
//        int rowDiff = Math.abs(curRow-prePiece.getRow());
//        int min = Math.min(colDiff,rowDiff);
//        int max = Math.max(colDiff,rowDiff);
//        return min == prePiece.getType().getMin() && max <= prePiece.getType().getMax()
//                && max >= 1  &&
                return curRow >= BoundaryEnum.BOARD_BOUNDARY.getBottom()
                && curRow <= BoundaryEnum.BOARD_BOUNDARY.getTop()
                && curCol >= BoundaryEnum.BOARD_BOUNDARY.getLeft()
                && curCol <= BoundaryEnum.BOARD_BOUNDARY.getRight();
    }

    protected abstract boolean pieceMove(Board board, Piece prePiece, int curCol, int curRow);
}
