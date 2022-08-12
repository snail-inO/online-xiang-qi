package me.portfolio.library.service;

import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.util.PieceColorEnum;
import me.portfolio.library.util.PieceTypeEnum;

public class PieceStrategyBingImpl extends GeneralPieceStrategyImpl{
    @Override
    protected boolean pieceMove(Board board, Piece prePiece, int curCol, int curRow) {
        int preRow = prePiece.getRow();
        int preCol = prePiece.getCol();
        if(curRow < preRow && prePiece.getColor() == PieceColorEnum.RED
                || curRow > preRow && prePiece.getColor() == PieceColorEnum.BLACK
        ) return false;
        //before river
        else if (preRow < 5 && prePiece.getColor() == PieceColorEnum.RED
                || preRow > 4 && prePiece.getColor() == PieceColorEnum.BLACK
        ) {
            if(preCol != curCol) return false;
            else if(curRow == preRow + PieceTypeEnum.BING.getMax() && prePiece.getColor() == PieceColorEnum.RED
                    || curRow == preRow - PieceTypeEnum.BING.getMax() && prePiece.getColor() == PieceColorEnum.BLACK
            ) return true;
            else return false;
        }

        //crossed river
        else {
            if(Math.abs(curCol-preCol) == PieceTypeEnum.BING.getMax()
                    && curRow == preRow
                    || Math.abs(curRow-preRow) == PieceTypeEnum.BING.getMax()
                    && curCol == preCol
            ) return true;
            else return false;
        }
    }
}
