package me.portfolio.application.service;

import me.portfolio.library.entity.Board;
import me.portfolio.library.entity.Game;

public interface BoardService {
    Board initBoard(Game game);
}
