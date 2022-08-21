package me.portfolio.library.service;

import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Piece;

public interface PieceStrategy {
    Piece move(Board board, Piece prePiece, int curCol, int curRow);
}
