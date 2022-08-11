package me.portfolio.library.service;

import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.util.BoundaryEnum;

public interface PieceStrategy {
    Piece move(Board board, Piece prePiece, int curCol, int curRow);
}
