package me.portfolio.library.service;

import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.util.PieceColorEnum;
import me.portfolio.library.util.PieceTypeEnum;

public class PieceStrategyShiImpl extends GeneralPieceStrategyImpl{
    @Override
    protected boolean pieceMove(Board board, Piece prePiece, int curCol, int curRow) {
        int preCol = prePiece.getCol();
        int preRow = prePiece.getRow();

        if(//inside 田 area
                (curRow > -1 && curRow < 3 && prePiece.getColor() == PieceColorEnum.RED
                || curRow > 6 && curRow < 10 && prePiece.getColor() == PieceColorEnum.BLACK)
                && curCol > 2 && curCol < 6
                //move valid
                && Math.abs(curCol - preCol) == PieceTypeEnum.SHI.getMax()
                && Math.abs(curRow - preRow) == PieceTypeEnum.SHI.getMax()
        ) return true;
        return false;
    }
}
