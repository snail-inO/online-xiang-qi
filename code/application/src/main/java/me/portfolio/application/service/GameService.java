package me.portfolio.application.service;

import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Game;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.entity.User;

import java.util.Collection;

public interface GameService {
    Game initGame(Collection<User> users);
    void updateGame(Board board, Piece prePiece, Piece curPiece);
}
