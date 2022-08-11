package me.portfolio.library.util;

import me.portfolio.library.entity.Piece;

public interface GetPieceIndex {
    static int getIndex(Piece piece) {
        return piece.getRow() * 9 + piece.getCol() - 1;
    }
}
