package me.portfolio.library.service;

import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Piece;

public class PieceStrategyPaoImpl extends GeneralPieceStrategyImpl {
    @Override
    protected boolean pieceMove(Board board, Piece prePiece, int curCol, int curRow) {
        int count = 0;
        boolean block = board.getPieces().containsKey(9 * curRow + curCol);
        if (prePiece.getCol() == curCol) {
            int min = Math.min(prePiece.getRow(), curRow);
            int max = Math.max(prePiece.getRow(), curRow);
            for (int i = min + 1; i < max; i++) {
                if (board.getPieces().containsKey(9 * i + curCol)) {
                    count++;
                    if (count > 1) return false;
                }
            }
            return (count == 0 && !block || count == 1 && block);
        } else if (prePiece.getRow() == curRow) {
            int min = Math.min(prePiece.getCol(), curCol);
            int max = Math.max(prePiece.getCol(), curCol);
            for (int i = min + 1; i < max; i++) {
                if (board.getPieces().containsKey(9 * curRow + i)) {
                    count++;
                    if (count > 1) return false;
                }
            }
            return (count == 0 && !block || count == 1 && block);
        }
        return false;
    }
}
