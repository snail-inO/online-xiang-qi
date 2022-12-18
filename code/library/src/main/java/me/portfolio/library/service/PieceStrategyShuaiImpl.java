package me.portfolio.library.service;

import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.util.PieceColorEnum;
import me.portfolio.library.util.PieceTypeEnum;

public class PieceStrategyShuaiImpl extends GeneralPieceStrategyImpl {
    @Override
    protected boolean pieceMove(Board board, Piece prePiece, int curCol, int curRow) {
        int preRow = prePiece.getRow();
        int preCol = prePiece.getCol();
        boolean block = false;
        //inside 田 area
        if ((curRow > -1 && curRow < 3 && prePiece.getColor() == PieceColorEnum.RED
                || curRow > 6 && curRow < 10 && prePiece.getColor() == PieceColorEnum.BLACK)
                && curCol > 2 && curCol < 6
        ) {
            int colAbs = Math.abs(preCol - curCol);
            int rowAbs = Math.abs(preRow - curRow);
            int min = Math.min(colAbs, rowAbs);
            int max = Math.max(colAbs, rowAbs);
            return min == PieceTypeEnum.SHUAI.getMin() && max == PieceTypeEnum.SHUAI.getMax();
        } else if (curCol == preCol && board.getPieces().containsKey(9 * curRow + curCol)
                && board.getPieces().get(9 * curRow + curCol).getType() == PieceTypeEnum.SHUAI) {
            int min = Math.min(preRow, curRow);
            int max = Math.max(preRow, curRow);
            for (int i = min + 1; i < max; i++) {
                if (board.getPieces().containsKey(9 * i + preCol)) {
                    block = true;
                    break;
                }
            }
            return !block;

        }
        return false;
    }

}
