package me.portfolio.library.service;

import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Piece;

public class PieceStrategyMaImpl extends GeneralPieceStrategyImpl {
    @Override
    protected boolean pieceMove(Board board, Piece prePiece, int curCol, int curRow) {
        int blockCol = -1;
        int blockRow = -1;
        boolean valid = false;
        if(Math.abs(prePiece.getRow()-curRow) == prePiece.getType().getMin()){
            //Left Block
            if(prePiece.getCol() - curCol == prePiece.getType().getMax()){
                blockRow = prePiece.getRow();
                blockCol = curCol + 1;
                valid = true;
            }
            //Right Block
            else if(prePiece.getCol() - curCol == -prePiece.getType().getMax()){
                blockRow = prePiece.getRow();;
                blockCol = curCol - 1;
                valid = true;
            }
        }
        else if(Math.abs(prePiece.getCol()-curCol) == prePiece.getType().getMin()){
            //Bottom Block
            if(prePiece.getRow() - curRow == prePiece.getType().getMax()){
                blockRow = curRow + 1;
                blockCol = prePiece.getCol();
                valid = true;
            }
            //Top Block
            else if(prePiece.getRow() - curRow == - prePiece.getType().getMax()){
                blockRow = curRow - 1;
                blockCol = prePiece.getCol();
                valid = true;
            }
        }
        return valid && !board.getPieces().containsKey(9 * blockRow + blockCol-1);
    }


}
