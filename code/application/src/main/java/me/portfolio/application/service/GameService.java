package me.portfolio.application.service;

import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Game;
import me.portfolio.library.entity.Piece;
import me.portfolio.library.entity.User;

import java.util.Collection;

public interface GameService {
    Game initGame(Collection<User> users);
    Game updateGame(Board board, Piece prePiece, Piece curPiece);
    Game endGame(Game game, User winner);
    void publishGameEvent(Game game);
}
