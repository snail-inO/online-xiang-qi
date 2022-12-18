package me.portfolio.application.service;

import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Game;
import me.portfolio.library.entity.Piece;

public interface BoardService {
    Board initBoard(Game game, int size);

    Board updateBoard(Board board, Piece prePiece, Piece curPiece);
}
