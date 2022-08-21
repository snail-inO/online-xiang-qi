package me.portfolio.library.service;

import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.util.PieceColorEnum;
import me.portfolio.library.util.PieceTypeEnum;

public class PieceStrategyXiangImpl extends GeneralPieceStrategyImpl {
    @Override
    protected boolean pieceMove(Board board, Piece prePiece, int curCol, int curRow) {

        int preCol = prePiece.getCol();
        int preRow = prePiece.getRow();
        if (//before river
                (curRow > -1 && curRow < 5 && prePiece.getColor() == PieceColorEnum.RED
                        || curRow > 4 && curRow < 10 && prePiece.getColor() == PieceColorEnum.BLACK)
                        //move valid
                        && Math.abs(curCol - preCol) == PieceTypeEnum.XIANG.getMax()
                        && Math.abs(curRow - preRow) == PieceTypeEnum.XIANG.getMax()
        ) return !board.getPieces().containsKey(9 * (curRow + preRow) / 2 + (curCol + preCol) / 2);
        return false;
    }
}
