package me.portfolio.library.service;

import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Piece;

public class PieceStrategyJuImpl extends GeneralPieceStrategyImpl{
    @Override
    protected boolean pieceMove(Board board, Piece prePiece, int curCol, int curRow) {

        boolean block = false;
        boolean valid = false;
        if(prePiece.getCol() == curCol){
            int min = Math.min(prePiece.getRow(),curRow);
            int max = Math.max(prePiece.getRow(),curRow);
            for(int i = min+1; i < max; i ++){
                    if(board.getPieces().containsKey(9*i+curCol)) {
                        block = true;
                    }
                }
            if(!block){
                valid = true;
            }
        }
        else if(prePiece.getRow() == curRow){
            int min = Math.min(prePiece.getCol(),curCol);
            int max = Math.max(prePiece.getCol(),curCol);
            for(int i = min+1; i < max; i ++){
                if(board.getPieces().containsKey(9*curRow+i)) {
                    block = true;
                }
            }
            if(!block){
                valid = true;
            }
        }

        return valid;
    }
}
